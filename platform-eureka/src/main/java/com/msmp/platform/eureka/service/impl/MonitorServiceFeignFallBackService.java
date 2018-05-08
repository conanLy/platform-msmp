package com.msmp.platform.eureka.service.impl;

import com.msmp.platform.common.service.BaseService;
import com.msmp.platform.common.vo.ServiceInstance;
import com.msmp.platform.eureka.service.IMonitorServiceFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MonitorServiceFeignFallBackService extends BaseService implements IMonitorServiceFeignClient {

    protected static final Logger logger = LoggerFactory.getLogger(MonitorServiceFeignFallBackService.class);

    @Override
    public void registerInstance(ServiceInstance instance) {
        logger.info("waiting for monitor-server reginster");
    }

    @Override
    public void renewInstance(ServiceInstance instance) {
        logger.info("waiting for monitor-server reginster");
    }

    @Override
    public void canceledInstance(String canceledMsg) {
        Map<String, String> param = getMapParam(canceledMsg);
        String serverKey = param.get("hostName") + "|" + param.get("port");
        redisTemplate.opsForHash().put("canceledInstance", serverKey, serverKey);
        logger.error("---监控服务宕机---");
        // TODO 告警监控服务宕机
    }


    private Map<String, String> getMapParam(String msg) {
        Map<String, String> param = new HashMap<>();
        String[] arr = msg.split(":");
        String hostName = null;
        String port = null;
        // 判断注册服务是否有服务名称
        if (arr.length == 2) {
            hostName = arr[0];
            port = arr[1];
        } else {
            hostName = arr[0];
            port = arr[2];
        }
        param.put("hostName", hostName);
        param.put("port", port);
        return param;
    }


}
