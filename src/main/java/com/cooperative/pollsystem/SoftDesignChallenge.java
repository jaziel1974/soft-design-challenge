package com.cooperative.pollsystem;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
@EnableMongoRepositories
public class SoftDesignChallenge {
	private static final Logger logger = LoggerFactory.getLogger(SoftDesignChallenge.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SoftDesignChallenge.class, args);
	}
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

		logger.info("Date in São Paulo: " + LocalDateTime.now());
	}
	
}
