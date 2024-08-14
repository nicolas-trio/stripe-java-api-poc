package com.trio.poc.stripe_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.trio.poc.stripe_demo.repository")
@EntityScan("com.trio.poc.stripe_demo.model")
@SpringBootApplication
public class StripeDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(StripeDemoApplication.class, args);
	}

}
