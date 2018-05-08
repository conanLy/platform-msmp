package com.msmp.platform.gateway.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IPlugDao {


    /**
     * 查询全局组件信息
     * @return 组件名称
     * @throws Exception
     */
    List<String> selectPlug() throws Exception;

    /**
     * 查询服务限流信息
     * @return 服务限流信息
     * @throws Exception
     */
    List<Map<String,String>> selectFlowPlug() throws Exception;

    /**
     * 查询IP过滤信息
     * @return ip过滤地址
     * @throws Exception
     */
    List<String> selectIpPlug() throws Exception;

    String selectPlugByKey(String param) throws Exception;

}
