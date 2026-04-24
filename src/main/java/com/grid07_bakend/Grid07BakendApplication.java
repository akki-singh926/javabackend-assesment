package com.grid07_bakend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Grid07BakendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Grid07BakendApplication.class, args);
	}

}
