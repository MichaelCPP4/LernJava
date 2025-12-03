package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000);
        SpringApplication.run(UserServiceApplication.class, args);
    }
}