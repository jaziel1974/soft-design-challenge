package com.cooperative.pollsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SoftDesignChallenge {

	public static void main(String[] args) {
		SpringApplication.run(SoftDesignChallenge.class, args);
	}

}
