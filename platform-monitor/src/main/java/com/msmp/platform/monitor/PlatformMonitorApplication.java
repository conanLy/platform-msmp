package com.msmp.platform.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PlatformMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformMonitorApplication.class, args);
	}
}
