package com.kotak.merchant.payments.gateway.service.Event_Management_Service.dao;

import com.kotak.merchant.payments.gateway.service.Event_Management_Service.model.CollectCallback;
import java.util.List;
import java.util.Optional;

public interface CollectCallbackDao {
    void create(CollectCallback collectCallback);

    List<CollectCallback> getByTransactionId(String transactionId);

    Optional<CollectCallback> getByTransactionIdAndType(String transactionId, String Type);
}
