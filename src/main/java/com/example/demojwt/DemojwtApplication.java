package com.example.demojwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemojwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemojwtApplication.class, args);
	}

}
