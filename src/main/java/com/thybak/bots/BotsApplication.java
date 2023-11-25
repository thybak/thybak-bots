package com.thybak.bots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BotsApplication {
	public static void main(String[] args) {
		SpringApplication.run(BotsApplication.class, args);
	}
}
