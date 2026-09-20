package org.gagandeepsuman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application class for the School Management System.
 * This replaces the original Main class as the entry point when running via Spring Boot.
 */
@SpringBootApplication
public class SistemaScuolaLingueApplication {

    public static void main(String[] args) {
        // System.setProperty("spring.classformat.ignore", "true");
        SpringApplication.run(SistemaScuolaLingueApplication.class, args);
    }
}