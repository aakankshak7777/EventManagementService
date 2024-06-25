package com.kmbl.eventmanagementservice.utils;


import com.kmbl.eventmanagementservice.service.Partitioned;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePartitioner implements Partitioner {

    private static final Logger log = LoggerFactory.getLogger(MessagePartitioner.class);

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if (value == null) {
            return 0;
        }
        var msg = (Partitioned) value;
        var topicPartitions = cluster.partitionCountForTopic(topic);
        var partitionKey = msg.partitionKey();
        var partition =
                Utils.toPositive(Utils.murmur2(partitionKey.getBytes(StandardCharsets.UTF_8))) % topicPartitions;
        //    var partition = BuiltInPartitioner.partitionForKey(
        //        partitionKey.getBytes(StandardCharsets.UTF_8),
        //        topicPartitions
        //    );
        log.trace("Partition chosen: {}, Key: {}, Topic partitions: {}", partition, partitionKey, topicPartitions);
        return partition;
    }

    @Override
    public void close() {
        // Nothing to close
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // Nothing to configure
    }
}
