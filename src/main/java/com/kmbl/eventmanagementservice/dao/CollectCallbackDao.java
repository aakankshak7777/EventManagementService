package com.kmbl.eventmanagementservice.dao;

import com.kmbl.eventmanagementservice.model.CollectCallback;
import java.util.Optional;

public interface CollectCallbackDao {
    void create(CollectCallback collectCallback);

    void update(CollectCallback collectCallback);

    Optional<CollectCallback> getByTransactionIdAndType(String transactionId, String Type);
}
