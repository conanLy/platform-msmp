package com.msmp.platform.eureka.convert;

import com.msmp.platform.common.vo.ServiceInstance;
import com.netflix.appinfo.InstanceInfo;

public class ServiceInstanceConvert {

    public static ServiceInstance instanceToServiceInstance(InstanceInfo info)
    {
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setAppName(info.getAppName().toUpperCase());
        serviceInstance.setHostName(info.getHostName());
        serviceInstance.setIpAddr(info.getIPAddr());
        serviceInstance.setPort(info.getPort() + "");
        serviceInstance.setVipAddress(info.getVIPAddress());
        return serviceInstance;
    }
}
