package com.kmbl.eventmanagementservice.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import software.amazon.awssdk.metrics.MetricCollection;
import software.amazon.awssdk.metrics.MetricPublisher;

public class AwsSdkClientMetricsPublisherConfig implements MetricPublisher, AutoCloseable {
    /*
    Reference Github Gist
    https://gist.github.com/PatrykGala/e4aec004eb55cd8cbdee328f217771c7
    */

    private static final List<String> IGNORED_TAGS = List.of("AwsExtendedRequestId", "AwsRequestId");
    private final AtomicInteger zero = new AtomicInteger();
    private static final String PREFIX = "awsSdk";
    private final ExecutorService service;
    private final MeterRegistry registry;
    private final Map<String, AtomicInteger> gauges = new ConcurrentHashMap<>();
    private final Tag name;

    public AwsSdkClientMetricsPublisherConfig(MeterRegistry registry) {
        this.registry = registry;
        this.service = Executors.newSingleThreadExecutor();
        this.name = Tag.of("name", "sdk-client-metrics");
    }

    @Override
    public void publish(MetricCollection metricCollection) {
        service.submit(() -> publishWithChildren(metricCollection));
    }

    private void publishWithChildren(MetricCollection metricCollection) {

        metricCollection.children().forEach(this::publishWithChildren);
        List<Tag> tags =
                Stream.concat(buildTags(metricCollection), Stream.of(name)).toList();
        metricCollection.stream()
                .filter(rec -> rec.value() instanceof Duration || rec.value() instanceof Integer)
                .forEach(rec -> {
                    String metricName = String.join(
                            ".", PREFIX, metricCollection.name(), rec.metric().name());
                    if (rec.value() instanceof Duration) {
                        Timer.builder(metricName)
                                .tags(tags)
                                .publishPercentiles(0.0, 0.75, 0.9, 0.95, 0.99, 1.0)
                                .publishPercentileHistogram()
                                .register(registry)
                                .record((Duration) rec.value());
                    } else if (rec.value() instanceof Integer) {
                        gauges.computeIfAbsent(metricName, key -> new AtomicInteger())
                                .set((Integer) rec.value());
                        registry.gauge(metricName, tags, metricName, key -> gauges.getOrDefault(key, zero)
                                .get());
                    }
                });
    }

    private Stream<Tag> buildTags(MetricCollection metricCollection) {
        return metricCollection.stream()
                .filter(rec -> rec.value() instanceof String || rec.value() instanceof Boolean)
                .filter(rec -> !IGNORED_TAGS.contains(rec.metric().name()))
                .map(rec -> Tag.of(rec.metric().name(), rec.value().toString()));
    }

    @Override
    public void close() {}
}
