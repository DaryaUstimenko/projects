package com.example.social_network_account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@EnableScheduling
public class SocialNetworkAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkAccountApplication.class, args);
	}

}
