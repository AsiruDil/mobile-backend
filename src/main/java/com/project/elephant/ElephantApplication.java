package com.project.elephant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


@SpringBootApplication
@EnableMongoAuditing
public class ElephantApplication {

	public static void main(String[] args) {

        SpringApplication.run(ElephantApplication.class, args);


	}
    
}
