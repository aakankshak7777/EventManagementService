package com.kmbl.eventmanagementservice.dao;

import com.kmbl.eventmanagementservice.model.CollectCallback;
import java.util.List;
import java.util.Optional;

public interface CollectCallbackDao {
    void create(CollectCallback collectCallback);

    List<CollectCallback> getByTransactionId(String transactionId);

    Optional<CollectCallback> getByTransactionIdAndType(String transactionId, String Type);
}
