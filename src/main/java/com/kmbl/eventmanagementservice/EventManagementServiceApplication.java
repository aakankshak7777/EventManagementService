package com.kmbl.eventmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kotak"})
public class EventManagementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventManagementServiceApplication.class, args);
    }
}
