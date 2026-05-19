package com.collectto.api_collectto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApiCollecttoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCollecttoApplication.class, args);
	}

}
