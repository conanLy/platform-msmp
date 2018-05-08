package com.msmp.platform.gateway.service;

import com.msmp.platform.common.service.BaseService;
import com.msmp.platform.gateway.dao.IPlugDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * 初始化所有组件
 */
@Service("globalPlugService")
public class GlobalPlugService extends BaseService {

    @Value("${plug.enable.name:plug-enable}")
    private String plugEnable;

    @Autowired
    private IPlugDao plugDao;

    @PostConstruct
    private void loadGlobalPlug(){
        try {
            // 预加载组件
            Long size = redisTemplate.opsForHash().size(plugEnable);
            System.out.println("loadPlug--- size:" + size);
            if(size == 0){
                List<String> plugs = plugDao.selectPlug();
                for(String s : plugs){
                    redisTemplate.opsForHash().put(plugEnable, s + "-enable", "1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearGlobalPlugCache(){
        redisTemplate.opsForHash().delete(plugEnable);
        loadGlobalPlug();
    }
}
