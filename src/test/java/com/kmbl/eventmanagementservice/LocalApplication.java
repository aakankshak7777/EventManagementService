package com.kmbl.eventmanagementservice;

import com.kmbl.eventmanagementservice.config.ContainerConfig;
import org.springframework.boot.SpringApplication;
import software.amazon.awssdk.regions.Region;

public class LocalApplication {

    public static void main(String[] args) {
        System.setProperty("aws.region", Region.AP_SOUTH_1.toString());
        System.setProperty("spring.main.allow-bean-definition-overriding", "true");
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.from(EventManagementServiceApplication::main).with(ContainerConfig.class).run(args);
    }
}
