package org.iotmapper.processing.streams.lpwan.lorawan;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.iotmapper.processing.models.metrics.GatewayLocation;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsAggregate;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;


/**
 * Filters Records on the basis of the timestamp compared to the last value in a StateStore
 */
public class LoRaWanMetricsAggregationTransformer implements Transformer<String, LoRaWanMetricsRecord, KeyValue<String, LoRaWanMetricsAggregate>> {
    private final String gwStoreName;
    private final String aggMetricStoreName;
    private KeyValueStore<String, GatewayLocation> gwStateStore;
    private KeyValueStore<String, LoRaWanMetricsAggregate> aggMetricStateStore;
    private KeyValueStore<String, GatewayLocation> stateStore;
    private ProcessorContext context;

    private Logger logger = LoggerFactory.getLogger(LoRaWanMetricsAggregationTransformer.class);

    // Configurable Parameters
    private boolean dropOldRecords = true;

    public LoRaWanMetricsAggregationTransformer(String gwStoreName, String aggMetricStoreName) {
        this.gwStoreName = gwStoreName;
        this.aggMetricStoreName = aggMetricStoreName;
    }

    public LoRaWanMetricsAggregationTransformer(String gwStoreName, String aggMetricStoreName, boolean dropOldRecords) {
        this.gwStoreName = gwStoreName;
        this.aggMetricStoreName = aggMetricStoreName;
        this.dropOldRecords = dropOldRecords;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        gwStateStore = this.context.getStateStore(gwStoreName);
        aggMetricStateStore = this.context.getStateStore(aggMetricStoreName);
    }

    @Override
    public KeyValue<String, LoRaWanMetricsAggregate> transform(String key, LoRaWanMetricsRecord metricsRecord) {
        LoRaWanMetricsAggregate aggregate;
        // Get real key
        String keyPair = metricsRecord.getCellIdGatewayIdPair().getIdString();
        // Check condition
        Instant recordTimestamp = metricsRecord.getTimeStamp();
        Instant storeTimestamp = gwStateStore.get(key).getTimestamp();
        //logger.warn("For keypair: {}: Record timestamp: {}, store timestamp: {}", keyPair, recordTimestamp, storeTimestamp);
        // If the record is not at least as recent as the value in the store cut it
        if (this.dropOldRecords && recordTimestamp.compareTo(storeTimestamp) < 0) {
            return null;
        }
        // Check if restart is needed
        if (metricsRecord.hasGatewayLocationUpdate()) {
            aggregate = LoRaWanMetricsAggregate.initialValue();
        } else {
            aggregate = aggMetricStateStore.get(keyPair);
            if (aggregate == null) // First read
                aggregate = LoRaWanMetricsAggregate.initialValue();
        }
        // Group-by assured by key
        // Aggregate
        aggregate.add(metricsRecord);
        // Write value
        aggMetricStateStore.put(keyPair, aggregate);

        return new KeyValue<>(keyPair, aggregate);
    }

    @Override
    public void close() {

    }
}
