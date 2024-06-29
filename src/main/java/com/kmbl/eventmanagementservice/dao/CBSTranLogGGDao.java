package com.kmbl.eventmanagementservice.dao;

import com.kmbl.eventmanagementservice.service.dtos.CBSTranLogGG;
import java.util.List;
import java.util.Optional;

public interface CBSTranLogGGDao {
    void create(CBSTranLogGG collectCallback);
    Optional<CBSTranLogGG> getByTransactionIdAndType(String transactionId, String Type);
}
