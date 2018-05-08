package com.msmp.platform.gateway.service;

import com.alibaba.fastjson.JSON;
import com.msmp.platform.common.service.BaseService;
import com.msmp.platform.gateway.dao.IPlugDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;


/**
 * 初始化IP过滤组件
 */
@Service("ipPlugService")
public class IpPlugService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(IpPlugService.class);

    @Value("${ip.plug.info.name:ip-plug-info}")
    private String ipPlugInfo;

    @Value("${ip.plug.key.name:ip-plug}")
    private String ipPlugKey;


    @Autowired
    private IPlugDao plugDao;

    @PostConstruct
    private void loadIpPlug(){
        String msg = redisTemplate.opsForValue().get(ipPlugInfo);
        try {
            if(!StringUtils.hasText(msg)) {
                List<String> ipAddressList = plugDao.selectIpPlug();
                redisTemplate.opsForValue().set(ipPlugInfo, JSON.toJSONString(ipAddressList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearIpPlugCache(){
        redisTemplate.opsForHash().delete(ipPlugInfo);
        loadIpPlug();
    }
}
