package com.project.elephant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableMongoAuditing
@EnableScheduling
public class ElephantApplication {

	public static void main(String[] args) {

        SpringApplication.run(ElephantApplication.class, args);


	}
    
}
