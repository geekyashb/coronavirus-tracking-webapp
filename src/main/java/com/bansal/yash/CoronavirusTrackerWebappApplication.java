package com.bansal.yash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoronavirusTrackerWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronavirusTrackerWebappApplication.class, args);
	}

}
