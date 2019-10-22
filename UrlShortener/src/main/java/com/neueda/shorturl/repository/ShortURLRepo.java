package com.neueda.shorturl.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import redis.clients.jedis.Jedis;

/*
 * This repository class is the brain of the URL Calculator, thereby saving 
 * map of long to short Urls & short to long Urls mapping 
 * */
@Repository
public class ShortURLRepo {
	@Value("${spring.redis.host}")
    private String REDIS_HOSTNAME;
	private final Jedis jedisShortToLong;
	private final Jedis jedisLongToShort;
	private final Jedis jedisShortToCount;
	private final String idKey;
	private final String urlKey;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShortURLRepo.class);

	public ShortURLRepo() {
		this.jedisShortToLong = new Jedis("redis");
		this.jedisLongToShort = new Jedis("redis");
		this.jedisShortToCount = new Jedis("redis");
		this.idKey = "id";
		this.urlKey = "url:";
	}

	public ShortURLRepo(Jedis jedisShortToLong,Jedis jedisLongToShort, Jedis jedisShortToCount, String idKey, String urlKey) {
		this.jedisShortToLong = jedisShortToLong;
		this.jedisLongToShort = jedisLongToShort;
		this.jedisShortToCount = jedisShortToCount;
		this.idKey = idKey;
		this.urlKey = urlKey;
	}

	public Long incrementID() {
		Long id = jedisShortToLong.incr(idKey);
		id = id+1000000l;
		LOGGER.debug("Incrementing ID: {}", id - 1);
		return id - 1;
	}

	public void saveUrl(String key, String longUrl) {
		LOGGER.info("Saving: {} at {}", longUrl, key);
		jedisShortToLong.hset(urlKey+"1", key, longUrl);
		jedisLongToShort.hset(urlKey, longUrl, key);
		jedisShortToCount.hset(urlKey, key, "1");
	}

	public String getLongUrl(String shortUrl) throws Exception {
		LOGGER.debug("Retrieving at {}", shortUrl);
		String url = jedisShortToLong.hget(urlKey+"1", shortUrl);
		jedisShortToCount.hincrBy(urlKey, shortUrl, 1);
		
		LOGGER.debug("Retrieved {} at {}", url, shortUrl);
		if (url == null) {
			throw new Exception("URL at key" + shortUrl + " does not exist");
		}
		return url;
	}

	public String getShortUrl(String longUrl){
		LOGGER.debug("Retrieving LongURL at {}", longUrl);
		String url = jedisLongToShort.hget(urlKey, longUrl);
		LOGGER.debug("Retrieved LongUrl {} at {}", url, longUrl);
		return url;
	}

	public String getShortUrlCounter(String shortUrl){
		LOGGER.debug("Retrieving Count for {}", shortUrl);
		String count = jedisShortToCount.hget(urlKey, shortUrl);
		LOGGER.debug("Retrieved Count for {} is {}", shortUrl, count);
		return count;
	}

}