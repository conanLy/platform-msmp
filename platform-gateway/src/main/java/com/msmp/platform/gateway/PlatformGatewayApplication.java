package com.msmp.platform.gateway;

import com.msmp.platform.gateway.filter.FlowFilter;
import com.msmp.platform.gateway.filter.IpFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;


@SpringCloudApplication
@EnableZuulProxy
public class PlatformGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformGatewayApplication.class, args);
	}

	@Bean
	public FlowFilter flowFilter(){
		return new FlowFilter();
	}

	@Bean
	public IpFilter ipFilter(){
		return new IpFilter();
	}

}
