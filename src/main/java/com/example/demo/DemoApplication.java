package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Running on Java: " + System.getProperty("java.version"));
		SpringApplication.run(DemoApplication.class, args);
	}

}
