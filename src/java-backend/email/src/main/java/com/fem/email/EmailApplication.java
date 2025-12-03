package com.fem.email;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
            .directory(".")
            .load();

        System.setProperty("KAFKA_ADDRESS", dotenv.get("KAFKA_ADDRESS"));
		
		SpringApplication.run(EmailApplication.class, args);
	}

}