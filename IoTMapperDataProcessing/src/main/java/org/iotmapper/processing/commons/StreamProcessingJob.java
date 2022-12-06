package org.iotmapper.processing.commons;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.iotmapper.processing.streams.ITopologyBuilder;
import org.slf4j.Logger;

import java.util.Properties;

public class StreamProcessingJob {
    private Topology topology;
    private Properties props;
    private KafkaStreams streamsApp;
    private Logger logger;

    public StreamProcessingJob(ITopologyBuilder topologyBuilder, Properties props, Logger logger) {
        this.topology = topologyBuilder.getTopology();
        this.props = props;
        this.streamsApp = new KafkaStreams(this.topology, props);
        this.logger = logger;
    }

    public void run() {
        this.logger.info("Starting StreamProcessingJob wrapper for KafkaStreams. With props: {}", this.props.toString());
        this.streamsApp.start();
    }

    public void stop() {
        this.logger.info("Stopping StreamProcessingJob wrapper for KafkaStreams. With props: {}", this.props.toString());
        this.streamsApp.close();
    }
}
