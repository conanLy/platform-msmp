package com.msmp.platform.gateway.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.msmp.platform.common.result.ResponseData;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 黑名单校验
 */
public class IpFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(IpFilter.class);

    @Value("${plug.enable.name:plug-enable}")
    private String plugEnable;

    @Value("${ip.plug.enable.name:ip-plug-enable}")
    private String ipPlugEnable;

    @Value("${ip.plug.info.name:ip-plug-info}")
    private String ipPlugInfo;

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 读取开关
        logger.info("BEGIN -- IpFilter -- shouldFilter()--  判断ip过滤组件总开关是否开启");
        Object obj = redisTemplate.opsForHash().get(plugEnable, ipPlugEnable);
        if (obj != null) {
            String enable = (String) obj;
            if ("1".equals(enable)) {
                logger.info(" -- IpFilter -- shouldFilter()--  ip过滤组件总开关已开启");
                return true;
            }
            logger.info(" -- FlowFilter -- shouldFilter()--  ip过滤组件总开关未开启");
        }
        return false;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        String ipAddr = this.getIpAddr(req);
        logger.info("请求IP地址为：{}", ipAddr);

        // 从DB中读取黑名单
        String msg = redisTemplate.opsForValue().get(ipPlugInfo);
        if(StringUtils.hasText(msg)){
            List<String> ipAddresslist = JSONArray.parseArray(msg, String.class);
            if (ipAddresslist.contains(ipAddr)) {
                logger.info("IpFilter -- 非法访问 --");
                ResponseData data = ResponseData.fail(HttpStatus.FORBIDDEN.getReasonPhrase(),  HttpStatus.FORBIDDEN.value());
                ctx.setResponseBody(JSON.toJSONString(data));
                ctx.getResponse().setContentType("application/json; charset=utf-8");
                ctx.setSendZuulResponse(false);
            } else {
                logger.info("IpFilter -- 合法访问 --");
            }
        }
        return null;
    }

    public String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
