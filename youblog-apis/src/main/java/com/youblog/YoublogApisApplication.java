package com.youblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YoublogApisApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoublogApisApplication.class, args);
	}

}
