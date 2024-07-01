package com.kmbl.eventmanagementservice.dao;

import com.kmbl.eventmanagementservice.service.dtos.CBSTranLog;
import java.util.Optional;

public interface CBSTranLogDao {
    void create(CBSTranLog cbsTranLog);
    void update(CBSTranLog cbsTranLog);
    Optional<CBSTranLog> getByTransactionIdAndType(String transactionId, String Type);
}
