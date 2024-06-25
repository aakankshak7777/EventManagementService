package com.kotak.merchant.payments.gateway.service.Event_Management_Service.Utils;

import java.util.UUID;

public class UUIDProvider {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
