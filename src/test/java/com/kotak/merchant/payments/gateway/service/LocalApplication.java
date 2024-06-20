package com.kotak.merchant.payments.gateway.service;

import com.kotak.merchant.payments.gateway.service.Config.ContainerConfig;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.EventManagementServiceApplication;
import org.springframework.boot.SpringApplication;
import software.amazon.awssdk.regions.Region;

public class LocalApplication {

    public static void main(String[] args) {
        System.setProperty("aws.region", Region.AP_SOUTH_1.toString());
        System.setProperty("spring.devtools.restart.enabled", "false");
        System.setProperty("spring.profiles.active", "local");
        SpringApplication.from(EventManagementServiceApplication::main).with(ContainerConfig.class).run(args);
    }
}
