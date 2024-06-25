package com.kotak.merchant.payments.gateway.service;

import com.kotak.merchant.payments.gateway.service.integration.config.ContainerConfig;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.EventManagementServiceApplication;
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
