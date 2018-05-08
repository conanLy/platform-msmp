package com.msmp.platform.eureka.service;

import com.msmp.platform.common.vo.ServiceInstance;
import com.msmp.platform.eureka.convert.ServiceInstanceConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service("eurekaService")
@EnableAsync
public class AsyncService {

    protected static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Autowired
    private IMonitorServiceFeignClient feignClient;

    @Async("myAsyncPool")
    public void handleRegisterServer(EurekaInstanceRegisteredEvent event) {
        logger.info("registerServer---appName:{}", event.getInstanceInfo().getAppName());

        ServiceInstance instance = ServiceInstanceConvert.instanceToServiceInstance(event.getInstanceInfo());
        feignClient.registerInstance(instance);
    }

    @Async("myAsyncPool")
    public void handleCanceledServer(EurekaInstanceCanceledEvent event) {
        logger.info("canceledServer---appName:{}", event.getAppName());

        feignClient.canceledInstance(event.getServerId());
    }


    @Async("myAsyncPool")
    public void handleRenewServer(EurekaInstanceRenewedEvent event) {
        logger.info("renewServer---serviceId:{}", event.getServerId());

        ServiceInstance instance = ServiceInstanceConvert.instanceToServiceInstance(event.getInstanceInfo());

        feignClient.renewInstance(instance);
    }


}
