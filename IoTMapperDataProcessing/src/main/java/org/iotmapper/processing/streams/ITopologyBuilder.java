package org.iotmapper.processing.streams;

import org.apache.kafka.streams.Topology;

public interface ITopologyBuilder {
    /**
     * Construct the required topology object
     *
     * @return a topology config for Kafka Streams
     */
    Topology getTopology();

    /**
     * Builds the input topic name for dm-by-service-path
     *
     * @return the topic name
     */
    default String geTopicName(String service, String subservice) {
        return service + "xffffx002f" + subservice;
    }
}
