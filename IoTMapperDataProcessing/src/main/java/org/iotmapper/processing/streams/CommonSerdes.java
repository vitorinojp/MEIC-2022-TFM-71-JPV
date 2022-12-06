package org.iotmapper.processing.streams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.iotmapper.processing.commons.utils.JsonDeserializer;
import org.iotmapper.processing.commons.utils.JsonSerializer;
import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessage;
import org.iotmapper.processing.models.metrics.CellIdGatewayIdPair;
import org.iotmapper.processing.models.metrics.GatewayLocation;
import org.iotmapper.processing.models.metrics.IMetricsRecord;
import org.iotmapper.processing.streams.serdes.CygnusTopicMessageSerde;
import org.slf4j.Logger;

/**
 * Abstracts the creation common serde types for simple reuse
 */
public class CommonSerdes {

    /**
     * A Serde object for string
     *
     * @return Serde<String>
     */
    public static Serde<String> getStringSerde() {
        return Serdes.String();
    }

    /**
     * A Serde object for CygnusTopicMessage
     *
     * @return Serde<CygnusTopicMessage>
     */
    public static Serde<CygnusTopicMessage> getCygnusTopicMessageSerce() {
        return new CygnusTopicMessageSerde();
    }

    /**
     * A Serde object for GatewayLocation
     *
     * @param logger
     * @return Serde<GatewayLocation>
     */
    public static Serde<GatewayLocation> getGatewayLocationSerde(Logger logger) {
        JsonSerializer<GatewayLocation> gatewayLocationJsonSerializer = new JsonSerializer<>(logger);
        JsonDeserializer<GatewayLocation> gatewayLocationJsonDeserializer = new JsonDeserializer<>(GatewayLocation.class, logger);
        return Serdes.serdeFrom(gatewayLocationJsonSerializer, gatewayLocationJsonDeserializer);
    }

    /**
     * A Serde object for IMetricsRecord
     *
     * @param logger
     * @return Serde<IMetricsRecord>
     */
    public static Serde<IMetricsRecord> getIMetricsRecordSerde(Logger logger) {
        JsonSerializer<IMetricsRecord> iMetricsRecordJsonSerializer = new JsonSerializer<>(logger);
        JsonDeserializer<IMetricsRecord> iMetricsRecordJsonDeserializer = new JsonDeserializer<>(IMetricsRecord.class, logger);
        return Serdes.serdeFrom(iMetricsRecordJsonSerializer, iMetricsRecordJsonDeserializer);
    }

    /**
     * A Serde object for CellIdGatewayIdPair
     *
     * @param logger
     * @return Serde<CellIdGatewayIdPair>
     */
    public static Serde<CellIdGatewayIdPair> getCellIdGatewayIdPairSerde(Logger logger) {
        JsonSerializer<CellIdGatewayIdPair> cellIdGatewayIdPairJsonSerializer = new JsonSerializer<>(logger);
        JsonDeserializer<CellIdGatewayIdPair> cellIdGatewayIdPairJsonDeserializer = new JsonDeserializer<>(CellIdGatewayIdPair.class, logger);
        return Serdes.serdeFrom(cellIdGatewayIdPairJsonSerializer, cellIdGatewayIdPairJsonDeserializer);
    }
}
