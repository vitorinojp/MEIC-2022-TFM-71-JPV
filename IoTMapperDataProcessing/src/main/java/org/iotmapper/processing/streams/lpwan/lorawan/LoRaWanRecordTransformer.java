package org.iotmapper.processing.streams.lpwan.lorawan;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.iotmapper.processing.models.metrics.GatewayLocation;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoRaWanRecordTransformer implements ValueTransformer<LoRaWanMetricsRecord, LoRaWanMetricsRecord> {
    private static final Logger logger = LoggerFactory.getLogger(LoRaWanRecordTransformer.class);
    private final String storeName;
    private KeyValueStore<String, GatewayLocation> stateStore;

    public LoRaWanRecordTransformer(String storeName) {
        this.storeName = storeName;
    }

    public GatewayLocation get(String gwId) {
        return stateStore.get(gwId);
    }

    public void set(String gwId, GatewayLocation gwLocation) {
        stateStore.put(gwId, gwLocation);
    }

    @Override
    public void init(ProcessorContext context) {
        stateStore = context.getStateStore(storeName);
    }

    @Override
    public LoRaWanMetricsRecord transform(LoRaWanMetricsRecord metricsRecord) {
        GatewayLocation last = stateStore.get(metricsRecord.getGatewayId());
        if (last == null || (last.distanceMoreThan(metricsRecord.getGatewayLocation(), metricsRecord.getMaxDistance())) ) {
            metricsRecord.setGatewayLocationUpdate(true);
            stateStore.put(metricsRecord.getGatewayId(), metricsRecord.getGatewayLocation());
        }

        return metricsRecord;
    }

    @Override
    public void close() {
        stateStore.close();
    }
}
