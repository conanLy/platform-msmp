package com.msmp.platform.monitor.service;

import com.alibaba.fastjson.JSON;
import com.msmp.platform.common.service.BaseService;
import com.msmp.platform.common.vo.ServiceInstance;
import com.msmp.platform.monitor.dao.MonitorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("monitorService")
public class MonitorService extends BaseService {

    protected static final Logger logger = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private MonitorDao monitorDao;

    @Transactional
    public void registerInstance(ServiceInstance instance) {
        logger.info("--registerInstance instance:{}--", JSON.toJSONString(instance));

        String serverKey = instance.getHostName() + "|" + instance.getPort();
        // 过滤无效数据
        int ret = Math.toIntExact(redisTemplate.opsForValue().increment(serverKey, 1L));
        if (ret == 1) {
            // 新注册用户
            instance.setStatus("1");
            instance.setUpdateTime(sdf.format(new Date()));
            try {
                // 根据ip port判断是否注册过
                int count = monitorDao.selectReginsterInstanceCount(instance);
                if (0 == count) {
                    monitorDao.insterRegisterInstance(instance);
                } else {
                    monitorDao.updateRegisterInstance(instance);
                }
            } catch (Exception e) {
                logger.error("--registerInstance() error--  进入补偿流程",e);
                redisTemplate.delete(serverKey);
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void canceledServerInstance(String canceledMsg) {
        logger.info("--canceledServerInstance canceledMsg:{}--", canceledMsg);

        Map<String, String> param = getMapParam(canceledMsg,":");
        ServiceInstance instance = new ServiceInstance();
        instance.setUpdateTime(sdf.format(new Date()));
        instance.setHostName(param.get("hostName"));
        instance.setPort(param.get("port"));
        instance.setStatus("0");

        String serverKey = param.get("hostName") + "|" + param.get("port");
        try {
            // 更新db
            monitorDao.updateRegisterInstance(instance);
            redisTemplate.delete(serverKey);
        } catch (Exception e) {
            logger.error("--canceledServerInstance() error--  进入补偿流程",e);
            redisTemplate.opsForHash().put("canceledInstance", serverKey, serverKey);
            e.printStackTrace();
        }
    }


    public void renewServerInstance(ServiceInstance instance) {
        logger.info("--renewServerInstance instance:{}--", JSON.toJSONString(instance));

        String serverKey = instance.getHostName() + "|" + instance.getPort();
        String objMsg = redisTemplate.opsForValue().get(serverKey);
        if (!StringUtils.hasText(objMsg)) {
            registerInstance(instance);
        }
        try {
            // 周期行的检查是否有被注销的服务
            if(redisTemplate.opsForHash().size("canceledInstance") > 0){
                Cursor<Map.Entry<Object, Object>> canceledMap =
                        redisTemplate.opsForHash().scan("canceledInstance", ScanOptions.NONE);
                while (canceledMap.hasNext()) {
                    Map.Entry<Object, Object> entry = canceledMap.next();
                    Map<String, String> param = getMapParam((String) entry.getValue(),"\\|");
                    ServiceInstance serviceInstance = new ServiceInstance();
                    serviceInstance.setUpdateTime(sdf.format(new Date()));
                    serviceInstance.setHostName(param.get("hostName"));
                    serviceInstance.setPort(param.get("port"));
                    serviceInstance.setStatus("0");
                    monitorDao.updateRegisterInstance(serviceInstance);
                }
                redisTemplate.delete("canceledInstance");
            }
        } catch (Exception e) {
            logger.error("--renewServerInstance() error-- ",e);
            e.printStackTrace();
        }
    }


    private Map<String, String> getMapParam(String msg, String symbol) {
        Map<String, String> param = new HashMap<>();
        String[] arr = msg.split(symbol);
        String serviceId = "UNKNOWN";
        String hostName = null;
        String port = null;
        // 判断注册服务是否有服务名称
        if (arr.length == 2) {
            hostName = arr[0];
            port = arr[1];
        } else {
            hostName = arr[0];
            serviceId = arr[1];
            port = arr[2];
        }
        param.put("serviceId", serviceId);
        param.put("hostName", hostName);
        param.put("port", port);
        return param;
    }


}
