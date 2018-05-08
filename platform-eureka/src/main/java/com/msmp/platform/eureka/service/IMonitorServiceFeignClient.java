package com.msmp.platform.eureka.service;


import com.msmp.platform.common.vo.ServiceInstance;
import com.msmp.platform.eureka.service.impl.MonitorServiceFeignFallBackService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "platform-monitor",fallback = MonitorServiceFeignFallBackService.class)
public interface IMonitorServiceFeignClient {

    @RequestMapping(value = "registerInstance", method = RequestMethod.POST)
    void registerInstance(@RequestBody ServiceInstance instance);

    @RequestMapping(value = "renewInstance", method = RequestMethod.POST)
    void renewInstance(@RequestBody ServiceInstance instance);

    @RequestMapping(value = "canceledInstance", method = RequestMethod.DELETE)
    void canceledInstance(@RequestParam("canceledMsg") String canceledMsg);

}
