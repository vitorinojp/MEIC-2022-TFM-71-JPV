package org.iotmapper.processing;

import org.apache.kafka.streams.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.test.TestRecord;
import org.iotmapper.processing.commons.configs.Configuration;
import org.iotmapper.processing.models.metrics.GatewayLocation;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsAggregate;
import org.iotmapper.processing.streams.CommonSerdes;
import org.iotmapper.processing.streams.lpwan.lorawan.LoRaWanTopology;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class IoTMapperDataProcessingTest {
    private static Logger logger = LoggerFactory.getLogger(IoTMapperDataProcessingTest.class);

    @Test
    public void getLoRaRecordsFromTestTopics() {

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        Configuration configuration = new Configuration();
        configuration.KAFKA_ID_BASE = "test-local";
        configuration.KAFKA_HOST = "localhost:9092";

        LoRaWanTopology loRaWanTopology = new LoRaWanTopology();

        Topology topology = loRaWanTopology.getTopology();

        // create test driver
        TopologyTestDriver testDriver = new TopologyTestDriver(topology);

        TestInputTopic<String, String> inputTopic = testDriver.createInputTopic(loRaWanTopology.getTopicName(), CommonSerdes.getStringSerde().serializer(), CommonSerdes.getStringSerde().serializer());
        inputTopic.pipeInput(null, TestStrings.test04);

        KeyValueStore<String, GatewayLocation> locationStore = testDriver.getKeyValueStore(Configuration.LORAWAN_GATEWAY_LOCATION_STATESTORE);
        KeyValue<String, GatewayLocation> pair = locationStore.all().next();

        TestRecord<String, String> record = testDriver.createOutputTopic("LoRaWAN_Aggregated_Metrics", CommonSerdes.getStringSerde().deserializer(), CommonSerdes.getStringSerde().deserializer()).readRecord();

        //assertEquals("test", pair.key);
        //assertNotNull(pair.value);
        //assertNull(pair.value.getPoint());

        KeyValueStore<String, LoRaWanMetricsAggregate> metricStore = testDriver.getKeyValueStore(Configuration.LORAWAN_METRICS_STATESTORE);
        KeyValue<String, LoRaWanMetricsAggregate> pair2 = metricStore.all().next();

        //assertEquals("test:eycs2x3w", pair2.key);
        //assertNotNull(pair2.value);
        //assertEquals(42.0, pair2.value.getLoRaWanMetrics().getRssi().get());
    }
}