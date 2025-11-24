package com.example.SADE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SadeApplication.class, args);

		ConsoleApp console = new ConsoleApp();
        console.run();
	}

}
