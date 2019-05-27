package org.openbpm.bpm.sys.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 配置
 *
 */
@ConfigurationProperties(prefix = "ab.redis")
public class RedisConfigProperties {

	/**
	 * 系统缓存是否使用 Redis:  false 则使用 系统内存  org.opembpm.base.core.cache.impl.MemoryCache ConcurrentHashMap
	 */
	private Boolean useRedisCache = Boolean.FALSE;

	public Boolean getUseRedisCache() {
		return useRedisCache;
	}

	public void setUseRedisCache(Boolean useRedisCache) {
		this.useRedisCache = useRedisCache;
	}
}
