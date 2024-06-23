package com.kotak.merchant.payments.gateway.service;

import com.kotak.merchant.payments.gateway.service.Config.ConfigForTests;
import com.kotak.merchant.payments.gateway.service.Config.ContainerConfig;
import com.kotak.merchant.payments.gateway.service.Config.SetupConfig;
import com.kotak.merchant.payments.gateway.service.Event_Management_Service.EventManagementServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.regions.Region;

public class LocalApplication {

    public static void main(String[] args) {
        System.setProperty("aws.region", Region.AP_SOUTH_1.toString());
        System.setProperty("spring.main.allow-bean-definition-overriding", "true");
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.from(EventManagementServiceApplication::main).with(ContainerConfig.class).run(args);
    }
}
