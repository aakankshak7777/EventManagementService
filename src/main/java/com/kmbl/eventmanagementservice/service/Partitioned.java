package com.kmbl.eventmanagementservice.service;

public interface Partitioned {

    String uniqueId();

    String partitionKey();
}
