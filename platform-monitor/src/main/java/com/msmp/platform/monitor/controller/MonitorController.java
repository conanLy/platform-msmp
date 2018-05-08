package com.msmp.platform.monitor.controller;

import com.msmp.platform.common.vo.ServiceInstance;
import com.msmp.platform.monitor.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "registerInstance", method = RequestMethod.POST)
    public void registerInstance(@RequestBody ServiceInstance instance){
        monitorService.registerInstance(instance);
    }

    @RequestMapping(value = "renewInstance", method = RequestMethod.POST)
    public void renewInstance(@RequestBody ServiceInstance instance){
        monitorService.renewServerInstance(instance);
    }

    @RequestMapping(value = "canceledInstance", method = RequestMethod.DELETE)
    public void canceledInstance(@RequestParam("canceledMsg") String canceledMsg){
        monitorService.canceledServerInstance(canceledMsg);
    }





}
