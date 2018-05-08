package com.msmp.platform.eureka.listener;

import com.msmp.platform.eureka.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class EurekaInstanceListener {

    @Autowired
    private AsyncService asyncService;

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        asyncService.handleRegisterServer(event);

    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        asyncService.handleRenewServer(event);

    }

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        asyncService.handleCanceledServer(event);
    }


}
