package com.neueda.shorturl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.neueda.shorturl.repository.ShortURLRepo;
import com.neueda.shorturl.utils.IDConvertor;

/**
 * @author parmaram
 *	This class acts as the interface between to get Long and Short URL 
 */
@Service
@Configuration
public class ShortURLService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShortURLService.class);
	private final ShortURLRepo shortRepo;

	@Autowired
    public ShortURLService(ShortURLRepo urlRepo) {
        this.shortRepo = urlRepo;
    }

	public String shortenURL(String localURL, String longUrl) {
		
		String domainURL = getDomainURL(localURL);
		String uniqueID=shortRepo.getShortUrl(longUrl);
		String shortUrl=""; 
		if(uniqueID==null) {
			LOGGER.info("Shortening {}", longUrl);
			Long id = shortRepo.incrementID();
			uniqueID = IDConvertor.INSTANCE.createUniqueID(id);
		}
		shortUrl = domainURL + uniqueID;
		shortRepo.saveUrl(uniqueID, longUrl);
		return shortUrl;
	}

	public String getLongURL(String shortURL) throws Exception {
		String longUrl = shortRepo.getLongUrl(shortURL);
		LOGGER.info("Converting shortened URL back to {}", longUrl);
		return longUrl;
	}
	
	public Long getStatsForShortUrl(String shortURL) throws Exception {
		String longcounter = shortRepo.getShortUrlCounter(shortURL);
		return Long.valueOf(longcounter);
	}

	private String getDomainURL(String localURL) {
	    int indexOfLastSlash = localURL.lastIndexOf('/');
	    String url = localURL.substring(0,indexOfLastSlash)+"/";
		return url;
	}
}
