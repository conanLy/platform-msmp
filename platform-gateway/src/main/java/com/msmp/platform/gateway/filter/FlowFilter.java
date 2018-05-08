package com.msmp.platform.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.msmp.platform.common.result.ResponseData;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 服务限流
 */
public class FlowFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(FlowFilter.class);

    @Value("${plug.enable.name:plug-enable}")
    private String plugEnable;

    @Value("${flow.plug.enable.name:flow-plug-enable}")
    private String flowPlugEnable;

    @Value("${flow.plug.info.name:flow-plug-info}")
    private String flowPlugInfo;


    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    @Override
    public int filterOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public boolean shouldFilter() {
        logger.info("BEGIN -- FlowFilter -- shouldFilter()--  判断服务限流总开关是否开启");
        Object obj = redisTemplate.opsForHash().get(plugEnable, flowPlugEnable);
        if (obj != null) {
            String enable = (String) obj;
            if ("1".equals(enable)) {
                logger.info(" -- FlowFilter -- shouldFilter()--  服务限流总开关已开启");
                return true;
            }
            logger.info(" -- FlowFilter -- shouldFilter()--  服务限流总开关未开启");
        }
        return false;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = (String) ctx.get("serviceId");
        if(StringUtils.hasText(serviceId)){
            // 判断服务是否开启限流
            String rMaxFlow = (String) redisTemplate.opsForHash().get(flowPlugInfo, serviceId);
            if (StringUtils.hasText(rMaxFlow)) {
                // 查询服务的限流信息
                String res = redisTemplate.opsForValue().get(serviceId + "-flow");
                if (StringUtils.hasText(res)) {
                    Long curFlow = redisTemplate.opsForValue().increment(serviceId + "-flow", 1L);
                    Long maxFlow = Long.valueOf(rMaxFlow);
                    if (curFlow >= maxFlow) {
                        ResponseData data = ResponseData.fail(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),  HttpStatus.TOO_MANY_REQUESTS.value());
                        ctx.setResponseBody(JSON.toJSONString(data));
                        ctx.getResponse().setContentType("application/json; charset=utf-8");
                        ctx.setSendZuulResponse(false);
                    } else {
                        redisTemplate.opsForValue().increment(serviceId + "-flow", 1L);
                    }
                } else {
                    redisTemplate.opsForValue().set(serviceId + "-flow", "0", 60, TimeUnit.SECONDS);
                }
            }
        }
        return null;
    }
}
