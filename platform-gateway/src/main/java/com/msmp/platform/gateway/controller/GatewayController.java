package com.msmp.platform.gateway.controller;

import com.msmp.platform.common.result.ResponseData;
import com.msmp.platform.gateway.config.RefreshRouteService;
import com.msmp.platform.gateway.service.FlowPlugService;
import com.msmp.platform.gateway.service.GlobalPlugService;
import com.msmp.platform.gateway.service.IpPlugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/local")
public class GatewayController {

    @Autowired
    private RefreshRouteService refreshRouteService;
    @Autowired
    private GlobalPlugService globalPlugService;
    @Autowired
    private IpPlugService ipPlugService;
    @Autowired
    private FlowPlugService flowPlugService;

    @RequestMapping(value = "/refreshRoute")
    public ResponseData refreshRoute() {
        ResponseData response = new ResponseData("error",500);
        try {
            refreshRouteService.refreshRoute();
            response = new ResponseData("ok",200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/clearGlobalPlugCache")
    public ResponseData clearGlobalPlugCache() {
        ResponseData response = new ResponseData("error",500);
        try {
            globalPlugService.clearGlobalPlugCache();
            response = new ResponseData("ok",200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  response;
    }

    @RequestMapping(value = "/clearFlowPlugCache")
    public ResponseData clearFlowPlugCache() {
        ResponseData response = new ResponseData("error",500);
        try {
            flowPlugService.clearFlowPlugCache();
            response = new ResponseData("ok",200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/clearIpPlugCache")
    public ResponseData clearIpPlugCache() {
        ResponseData response = new ResponseData("error",500);
        try {
            ipPlugService.clearIpPlugCache();
            response = new ResponseData("ok",200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
