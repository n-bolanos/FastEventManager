package com.fem.authentication;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticationApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
            .directory(".")
            .load();

        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("AUTH_PASSWORD", dotenv.get("AUTH_PASSWORD"));
        System.setProperty("KAFKA_ADDRESS", dotenv.get("KAFKA_ADDRESS"));

		SpringApplication.run(AuthenticationApplication.class, args);
	}

}
