package com.kotak.merchant.payments.gateway.service.Event_Management_Service.utils;

import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;

public class PartitionUtils {

    /**
     * Generate a partition ID based on the given partition key. If the key is null, then a random
     * partition is chosen. The returned value is not expected to be stable across process restarts,
     * i.e., the same key may give a different value if run across two different JVMs.
     *
     * @param partitionKey       Partition key to map to a given partition
     * @param inMemoryPartitions Number of partitions
     * @return A number between 0 (incl) and inMemoryPartitions (excl)
     */
    public static int inMemoryPartitionId(@Nullable Object partitionKey, int inMemoryPartitions) {
        if (partitionKey == null) {
            return ThreadLocalRandom.current().nextInt(0, inMemoryPartitions);
        }
        // This hash need not be stable across process restarts. So we can use
        // the simple hashcode implementation of the object itself.
        var hash = partitionKey.hashCode();
        if (hash == Integer.MIN_VALUE) {
            hash = 0;
        }
        return Math.abs(hash) % inMemoryPartitions;
    }
}
