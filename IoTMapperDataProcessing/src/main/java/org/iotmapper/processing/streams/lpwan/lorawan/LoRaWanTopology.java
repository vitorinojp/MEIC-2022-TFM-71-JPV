package org.iotmapper.processing.streams.lpwan.lorawan;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.iotmapper.processing.commons.configs.Configuration;
import org.iotmapper.processing.models.metrics.GatewayLocation;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsAggregate;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsRecord;
import org.iotmapper.processing.streams.AddHeadersTransformerSupplier;
import org.iotmapper.processing.streams.CommonSerdes;
import org.iotmapper.processing.streams.ITopologyBuilder;
import org.iotmapper.processing.streams.mapping.AggregateToFiwareBatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class LoRaWanTopology implements ITopologyBuilder {
    private Configuration configuration;
    private MapToLoraWanRecordType mapToLoraWanRecordType;
    private Logger logger = LoggerFactory.getLogger(LoRaWanTopology.class);
    // State Stores
    private KeyValueBytesStoreSupplier gwStoreSupplier = Stores.persistentKeyValueStore(Configuration.LORAWAN_GATEWAY_LOCATION_STATESTORE);
    private KeyValueBytesStoreSupplier aggMetricStoreSupplier = Stores.persistentKeyValueStore(Configuration.LORAWAN_METRICS_STATESTORE);

    public LoRaWanTopology() {
        this.mapToLoraWanRecordType = MapToLoraWanRecordType.get();
    }

    /**
     * Adds the necessary state stores to the topology to make them available always
     *
     * @param streamsBuilder the StreamsBuilder to use
     * @return the same StreamsBuilder that was provided
     */
    public StreamsBuilder addStateStores(StreamsBuilder streamsBuilder) {
        StoreBuilder<KeyValueStore<String, GatewayLocation>> gwStoreBuilder
                = Stores.keyValueStoreBuilder(gwStoreSupplier,
                CommonSerdes.getStringSerde(),
                CommonSerdes.getGatewayLocationSerde(logger)
        );
        StoreBuilder<KeyValueStore<String, LoRaWanMetricsAggregate>> aggMetricsStoreBuilder
                = Stores.keyValueStoreBuilder(aggMetricStoreSupplier,
                CommonSerdes.getStringSerde(),
                LoRaWanSerdes.getLoRaWanMetricsAggregateSerde(logger)
        );
        return streamsBuilder
                .addStateStore(gwStoreBuilder)
                .addStateStore(aggMetricsStoreBuilder);
    }

    /**
     * Creates the KStream that represents the mapping stage of the LoRaWanTopology
     *
     * @param streamsBuilder the StreamsBuilder to use
     * @return KStream with the result of mapping
     */
    public KStream<String, LoRaWanMetricsRecord> getMappingStage(StreamsBuilder streamsBuilder) {
        return streamsBuilder
                .stream(this.geTopicName(Configuration.LORAWAN_SERVICE, Configuration.LORAWAN_SERVICE_PATH),
                        Consumed.with(
                                CommonSerdes.getStringSerde(),
                                CommonSerdes.getCygnusTopicMessageSerce())
                )
                .flatMapValues(mapToLoraWanRecordType::map)
                .selectKey((key, record) -> record.getGatewayId())
                .repartition(Repartitioned
                        .<String, LoRaWanMetricsRecord>as(Configuration.LORAWAN_PARTITIONED_TOPIC_NAME)
                        .withKeySerde(CommonSerdes.getStringSerde())
                        .withValueSerde(LoRaWanSerdes.getLoRaWanMetricsRecordSerde(logger))
                );
    }

    /**
     * Check and updates the GATEWAY_LOCATION_STATESTORE
     *
     * @param cygnusMessageStream previous stage
     * @return the result of the new stage
     */
    public KStream<String, LoRaWanMetricsRecord> getGatewayUpdateStage(KStream<String, LoRaWanMetricsRecord> cygnusMessageStream) {
        return cygnusMessageStream
                .transformValues(() -> new LoRaWanRecordTransformer(Configuration.LORAWAN_GATEWAY_LOCATION_STATESTORE), Configuration.LORAWAN_GATEWAY_LOCATION_STATESTORE);
    }

    /**
     * Creates the KStream with the list of aggregations
     *
     * @param messageStream the KStream that contains the received messages
     * @return KStream<String, IMetricsAggregate> with the result, aggregations of metrics (IMetricsAggregate) indexed by the string value of CellIdGatewayIdPair
     */
    public KStream<String, LoRaWanMetricsAggregate> getAggregationStage(KStream<String, LoRaWanMetricsRecord> messageStream) {
        return messageStream
                .transform(() ->
                                new LoRaWanMetricsAggregationTransformer(
                                        Configuration.LORAWAN_GATEWAY_LOCATION_STATESTORE,
                                        Configuration.LORAWAN_METRICS_STATESTORE,
                                        Configuration.IOTMAPPER_DROP_OLD_RECORDS_VALUE
                                ),
                        Configuration.LORAWAN_GATEWAY_LOCATION_STATESTORE,
                        Configuration.LORAWAN_METRICS_STATESTORE
                );
    }

    private KStream<String, String> getOutputStage(KStream<String, LoRaWanMetricsAggregate> metricsStream) {
        return metricsStream
                .mapValues(new AggregateToFiwareBatchMapper())
                .transform(new AddHeadersTransformerSupplier("application/json", "lorawan", "/ttnmapper"));
    }

    /**
     * Output aggregation stream to configured topic
     *
     * @param aggregation the KStream that contains the aggregated metrics, index by string value of CellIdGatewayIdPair
     */
    public void writeOut(KStream<String, String> aggregation) {
        aggregation.to(
                Configuration.LORAWAN_AGGREGATION_TOPIC_NAME,
                Produced.with(
                        CommonSerdes.getStringSerde(),
                        CommonSerdes.getStringSerde()
                )
        );
    }


    @Override
    public Topology getTopology() {
        // *** Define running topology for computing *** //
        // ** Stream builder
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // * State stores
        streamsBuilder = this.addStateStores(streamsBuilder);
        // ** Topology
        // * Get Records from topics
        KStream<String, LoRaWanMetricsRecord> cygnusMessageStream = this.getMappingStage(streamsBuilder);
        //cygnusMessageStream.print(Printed.<String, LoRaWanMetricsRecord>toSysOut().withLabel("cygnusMessageStream"));
        // * Filter Gateway Location Updates
        KStream<String, LoRaWanMetricsRecord> gwUpdateStream = this.getGatewayUpdateStage(cygnusMessageStream);
        //gwUpdateStream.print(Printed.<String, LoRaWanMetricsRecord>toSysOut().withLabel("gwUpdateStream"));
        // * Aggregate updates
        KStream<String, LoRaWanMetricsAggregate> metricUpdateStream = this.getAggregationStage(gwUpdateStream);
        //metricUpdateStream.print(Printed.<String, LoRaWanMetricsAggregate>toSysOut().withLabel("metricUpdateStream"));
        // * Format as fiware entity
        KStream<String, String> outputStream = this.getOutputStage(metricUpdateStream);
        // * Write out
        this.writeOut(outputStream);

        return streamsBuilder.build();
    }

    public Properties getKafkaStreamsConfig() {
        String kafkaBroker = Configuration.KAFKA_HOST;
        String id = Configuration.KAFKA_ID_BASE + Configuration.KAFKA_ID_LORAWAN;

        Properties props = new Properties();

        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, id);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, CommonSerdes.getStringSerde().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, CommonSerdes.getCygnusTopicMessageSerce().getClass().getName());

        props.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, Configuration.KAFKA_REQUEST_TIMEOUT_MS_CONFIG);
        props.put(StreamsConfig.RETRY_BACKOFF_MS_CONFIG, Configuration.KAFKA_RETRY_BACKOFF_MS_CONFIG);
        props.put(StreamsConfig.STATE_DIR_CONFIG, Configuration.KAFKA_STATE_STORE_DIR);

        return props;
    }

    public String getTopicName() {
        return this.geTopicName(Configuration.LORAWAN_SERVICE, Configuration.LORAWAN_SERVICE_PATH);
    }

}
