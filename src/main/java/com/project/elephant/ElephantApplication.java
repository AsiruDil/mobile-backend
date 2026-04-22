package com.project.elephant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


@SpringBootApplication
public class ElephantApplication {

	public static void main(String[] args) {

        SpringApplication.run(ElephantApplication.class, args);


	}
    
}
