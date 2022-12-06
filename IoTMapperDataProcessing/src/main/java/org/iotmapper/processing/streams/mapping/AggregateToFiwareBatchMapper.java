package org.iotmapper.processing.streams.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.streams.kstream.ValueMapperWithKey;
import org.iotmapper.processing.commons.utils.GeoTools;
import org.iotmapper.processing.models.metrics.IMetricsAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AggregateToFiwareBatchMapper implements ValueMapperWithKey<String, IMetricsAggregate, String> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String apply(String s, IMetricsAggregate iMetricsAggregate) {
        String[] halves = s.split(":");
        String gwId = halves[0];
        String mapTile = halves[1];
        String geojson;
        try {
            geojson = GeoTools.decodeGeoHash(mapTile).toGeoJson();
        } catch (JsonProcessingException e) {
            this.logger.error("Couldn't get Geojson for Aggregation. id= {}. With reason: {}", s, e.getMessage());
            return null;
        }
        return "{"
                + "\"id\": \"" + s + "\","
                + "\"type\": \"MeasuresAggregation\"" + ","
                + "\"measures\": [" + iMetricsAggregate.getMeasures() + "]" + ","
                + "\"gwId\": " + "\"" + gwId + "\","
                + "\"mapTile\":" + "\"" + mapTile + "\","
                + "\"location\": " + geojson + ","
                + "\"dateLastGwUpdate\": \"" + iMetricsAggregate.getDateLastGwUpdate() + "\","
                + "\"dateLastUpdate\": \"" + iMetricsAggregate.getDateLastUpdate() + "\""
                + "}";
    }
}
