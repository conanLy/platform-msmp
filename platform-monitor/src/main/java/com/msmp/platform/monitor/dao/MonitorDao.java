package com.msmp.platform.monitor.dao;

import com.msmp.platform.common.vo.ServiceInstance;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorDao {

    void insterRegisterInstance(ServiceInstance instance) throws Exception;

    int selectReginsterInstanceCount(ServiceInstance instance) throws Exception;

    void updateRegisterInstance(ServiceInstance instance) throws Exception;




}
