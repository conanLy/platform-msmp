package com.msmp.platform.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class BaseService {

    protected static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    public RedisTemplate<String, String> redisTemplate;
}
