package com.kotak.merchant.payments.gateway.service.integration;

import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class IntegrationTestBase {

    public static final String AWS_REGION = "ap-south-1";

    static {
        System.setProperty("aws.region", AWS_REGION);
    }
}
