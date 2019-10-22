package com.neueda.shorturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.neueda.shorturl.repository.ShortURLRepo;

@SpringBootApplication
public class ShortURLApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortURLApplication.class, args);
	}

}
