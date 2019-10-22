package com.neueda.shorturl.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neueda.shorturl.service.ShortURLService;
import com.neueda.shorturl.utils.URLValidator;

@RestController
public class ShortURLController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShortURLController.class);
	private final ShortURLService shortURLService;

	public ShortURLController(ShortURLService shortURLService) {
		this.shortURLService = shortURLService;
	}
	
	//This Api does analytics and returns the number of times a given Url has been hit
	@RequestMapping(value="/stats/{shorturl}")
	public Long sayHello(@PathVariable String shorturl) throws Exception {
		Long count = shortURLService.getStatsForShortUrl(shorturl);
		
		return count;	
	}
	
	//This Api redirects given shortUrl to the actual mapped longUrl
	@RequestMapping(value = "/shortener", method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<String> shortenUrl(@RequestBody @Valid final ShortenRequestBean shortenRequest,
			HttpServletRequest request) throws Exception {
		LOGGER.debug("Received url to shorten: " + shortenRequest.getUrl());
		String longUrl = shortenRequest.getUrl();
		if (URLValidator.INSTANCE.validateURL(longUrl)) {
			String localURL = request.getRequestURL().toString();
			String shortenedUrl = shortURLService.shortenURL(localURL, shortenRequest.getUrl());
			LOGGER.debug("Shortened url to: " + shortenedUrl);
			return ResponseEntity.ok(shortenedUrl);
		}
		throw new Exception("Invalid URL format, please enter a valid URL");
	}

	//THis method returns a unique short Url for a given LongUrl
	// Its smart enough and returns you the same short URL given the same long URL 
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> redirectUrl(@PathVariable String id, HttpServletRequest request,
			HttpServletResponse response) throws IOException, URISyntaxException, Exception {
		LOGGER.debug("Received shortened url to redirect: " + id);
		String redirectUrlString = shortURLService.getLongURL(id);
		LOGGER.debug("Original URL: " + redirectUrlString);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(redirectUrlString));
		return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
	}
}

class ShortenRequestBean {
	private String url;

	@JsonCreator
	public ShortenRequestBean() {

	}

	@JsonCreator
	public ShortenRequestBean(@JsonProperty("url") String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
