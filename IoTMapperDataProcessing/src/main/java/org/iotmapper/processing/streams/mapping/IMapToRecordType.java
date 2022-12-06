package org.iotmapper.processing.streams.mapping;

import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessage;
import org.iotmapper.processing.models.metrics.IMetricsRecord;

public interface IMapToRecordType<R extends IMetricsRecord> {
    /**
     * Maps a given [CygnusTopicMessage] to a list of [R] type records, that extend [IMetricsRecord]
     *
     * @param cygnusTopicMessage the input message to map
     * @return the records that were mapped
     */
    Iterable<R> map(CygnusTopicMessage cygnusTopicMessage);
}
