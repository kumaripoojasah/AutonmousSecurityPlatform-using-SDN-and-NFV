package com.example.demo;


// SdnServiceBackendApplication.java serves as the entry point and central bootstrapped launcher for your entire Spring Boot microservice.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SdnServiceBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SdnServiceBackendApplication.class, args);
    }
}