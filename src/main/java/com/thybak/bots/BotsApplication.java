package com.thybak.bots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
public class BotsApplication {
	private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";

	@Bean
	public Clock clock() {
		return Clock.system(ZoneId.of(SPAIN_LOCAL_ZONE));
	}

	public static void main(String[] args) {
		SpringApplication.run(BotsApplication.class, args);
	}
}
