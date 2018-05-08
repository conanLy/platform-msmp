package com.msmp.platform.eureka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: liuyu
 * @Description:
 * @Date: Created in 2017/8/15 9:18
 * @Modified By:
 */
@Configuration
@EnableAsync
public class MyExecutePool
{
	@Autowired
	private MyThreadPoolConfig config;

	@Bean
	public Executor myAsyncPool() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(config.getCorePoolSize());
		executor.setMaxPoolSize(config.getMaxPoolSize());
		executor.setQueueCapacity(config.getQueueCapacity());
		executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
		executor.setThreadNamePrefix("myExecute-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
