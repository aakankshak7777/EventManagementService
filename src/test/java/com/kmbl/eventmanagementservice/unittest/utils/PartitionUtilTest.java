package com.kmbl.eventmanagementservice.unittest.utils;

import com.kmbl.eventmanagementservice.utils.PartitionUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PartitionUtilTest {

    @Test
    void inMemoryPartitionId_NullPartitionKey_ReturnsRandomPartition() {
        int inMemoryPartitions = 10;
        var partitionId = PartitionUtil.inMemoryPartitionId(null, inMemoryPartitions);
        Assertions.assertTrue(partitionId >= 0 && partitionId < inMemoryPartitions);
    }
    @Test
    void inMemoryPartitionId_NonNullPartitionKey_ReturnsStablePartition() {
        int inMemoryPartitions = 10;
        var partitionKey = new Object();
        var partitionId1 = PartitionUtil.inMemoryPartitionId(partitionKey, inMemoryPartitions);
        var partitionId2 = PartitionUtil.inMemoryPartitionId(partitionKey, inMemoryPartitions);
        Assertions.assertEquals(partitionId1, partitionId2);
    }

    @Test
    void inMemoryPartitionId_IntegerMinValue_DoesNotThrowException() {
        int inMemoryPartitions = 10;
        var partitionKey = Integer.MIN_VALUE;
        var partitionId = PartitionUtil.inMemoryPartitionId(partitionKey, inMemoryPartitions);
        Assertions.assertTrue(partitionId >= 0 && partitionId < inMemoryPartitions);
    }

    @Test
    void inMemoryPartitionId_RandomKeys_DistributedPartitions() {
        int inMemoryPartitions = 10;
        int numKeys = 1000;
        Set<Integer> partitionIds = new HashSet<>();
        for (int i = 0; i < numKeys; i++) {
            partitionIds.add(PartitionUtil.inMemoryPartitionId(new Object(), inMemoryPartitions));
        }
        Assertions.assertEquals(inMemoryPartitions, partitionIds.size());
    }
}
