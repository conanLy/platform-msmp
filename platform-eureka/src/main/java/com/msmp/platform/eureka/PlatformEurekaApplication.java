package com.msmp.platform.eureka;

import com.msmp.platform.eureka.config.MyThreadPoolConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaServer
@SpringBootApplication
@EnableConfigurationProperties({MyThreadPoolConfig.class} )
public class PlatformEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformEurekaApplication.class, args);
	}
}
