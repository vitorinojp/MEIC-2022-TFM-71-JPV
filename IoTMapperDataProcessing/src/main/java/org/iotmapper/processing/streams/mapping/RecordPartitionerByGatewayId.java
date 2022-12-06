package org.iotmapper.processing.streams.mapping;

import org.apache.kafka.streams.processor.StreamPartitioner;
import org.iotmapper.processing.models.metrics.IMetricsRecord;

public class RecordPartitionerByGatewayId implements StreamPartitioner<String, IMetricsRecord> {
    @Override
    public Integer partition(String s, String s2, IMetricsRecord metricsRecord, int numPartitions) {
        return metricsRecord.getGatewayId().hashCode() % numPartitions;
    }
}
