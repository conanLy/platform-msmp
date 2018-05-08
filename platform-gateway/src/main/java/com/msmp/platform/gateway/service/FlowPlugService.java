package com.msmp.platform.gateway.service;

import com.msmp.platform.common.service.BaseService;
import com.msmp.platform.gateway.dao.IPlugDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;


/**
 * 初始化服务限流组件
 */
@Service("flowPlugService")
public class FlowPlugService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(FlowPlugService.class);

    @Value("${flow.plug.info.name:flow-plug-info}")
    private String flowPlugInfo;

    @Value("${flow.plug.key.name:flow-plug}")
    private String flowPlugKey;


    @Autowired
    private IPlugDao plugDao;

    @PostConstruct
    private void loadFlowPlug(){
        Long size = redisTemplate.opsForHash().size(flowPlugInfo);
        if(size == 0){
            try {
                List<Map<String, String>> maps = plugDao.selectFlowPlug();
                for(Map<String,String> map : maps){
                    // 次/秒
                    String flowCount =  map.get("flowCount");
                    int maxCount = Integer.valueOf(flowCount) * 60;
                    redisTemplate.opsForHash().put(flowPlugInfo, map.get("serviceId"),maxCount+"");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void clearFlowPlugCache(){
        redisTemplate.opsForHash().delete(flowPlugInfo);
        loadFlowPlug();
    }
}
