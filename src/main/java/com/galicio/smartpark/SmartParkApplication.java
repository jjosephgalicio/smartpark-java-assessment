package com.galicio.smartpark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enables background scheduler for 15-min auto-removal
public class SmartParkApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartParkApplication.class, args);
    }
}