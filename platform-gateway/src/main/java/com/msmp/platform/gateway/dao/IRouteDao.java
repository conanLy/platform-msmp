package com.msmp.platform.gateway.dao;

import com.msmp.platform.gateway.entity.ZuulRouteEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRouteDao {

    List<ZuulRouteEntity> selectRoute();

}
