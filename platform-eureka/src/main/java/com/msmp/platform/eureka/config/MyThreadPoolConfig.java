package com.msmp.platform.eureka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: liuyu
 * @Description:
 * @Date: Created in 2017/8/15 9:17
 * @Modified By:
 */
@ConfigurationProperties(prefix = "my.async.thread.pool")
public class MyThreadPoolConfig
{
	private int corePoolSize;

	private int maxPoolSize;

	private int keepAliveSeconds;

	private int queueCapacity;

	public int getCorePoolSize ()
	{
		return corePoolSize;
	}

	public void setCorePoolSize (int corePoolSize)
	{
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize ()
	{
		return maxPoolSize;
	}

	public void setMaxPoolSize (int maxPoolSize)
	{
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveSeconds ()
	{
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds (int keepAliveSeconds)
	{
		this.keepAliveSeconds = keepAliveSeconds;
	}

	public int getQueueCapacity ()
	{
		return queueCapacity;
	}

	public void setQueueCapacity (int queueCapacity)
	{
		this.queueCapacity = queueCapacity;
	}
}
