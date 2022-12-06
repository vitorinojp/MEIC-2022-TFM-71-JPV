package org.iotmapper.processing.streams.lpwan.lorawan;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.iotmapper.processing.commons.utils.JsonDeserializer;
import org.iotmapper.processing.commons.utils.JsonSerializer;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsAggregate;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsRecord;
import org.slf4j.Logger;

/**
 * Abstracts the serde types used in LoRaWan topologies
 */
public class LoRaWanSerdes {
    /**
     * A Serde object for LoRaWanMetricsRecord
     *
     * @param logger
     * @return Serde<LoRaWanMetricsRecord>
     */
    public static Serde<LoRaWanMetricsRecord> getLoRaWanMetricsRecordSerde(Logger logger) {
        JsonSerializer<LoRaWanMetricsRecord> loRaWanMetricsRecordJsonSerializer = new JsonSerializer<>(logger);
        JsonDeserializer<LoRaWanMetricsRecord> loRaWanMetricsRecordJsonDeserializer = new JsonDeserializer<>(LoRaWanMetricsRecord.class, logger);
        return Serdes.serdeFrom(loRaWanMetricsRecordJsonSerializer, loRaWanMetricsRecordJsonDeserializer);
    }

    /**
     * A Serde object for LoRaWanMetricsAggregate
     *
     * @param logger
     * @return Serde<LoRaWanMetricsAggregate>
     */
    public static Serde<LoRaWanMetricsAggregate> getLoRaWanMetricsAggregateSerde(Logger logger) {
        JsonSerializer<LoRaWanMetricsAggregate> loRaWanMetricsRecordJsonSerializer = new JsonSerializer<>(logger);
        JsonDeserializer<LoRaWanMetricsAggregate> loRaWanMetricsRecordJsonDeserializer = new JsonDeserializer<>(LoRaWanMetricsAggregate.class, logger);
        return Serdes.serdeFrom(loRaWanMetricsRecordJsonSerializer, loRaWanMetricsRecordJsonDeserializer);
    }
}
