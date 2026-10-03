package com.marketplace.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import okhttp3.OkHttpClient;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class MarketplaceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketplaceApiApplication.class, args);
	}
	
	@Bean
    public OkHttpClient restTemplate() {
		return new OkHttpClient();
    }


}
