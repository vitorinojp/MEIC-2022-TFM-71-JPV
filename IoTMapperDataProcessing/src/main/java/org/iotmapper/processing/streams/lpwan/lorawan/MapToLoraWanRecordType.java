package org.iotmapper.processing.streams.lpwan.lorawan;

import org.iotmapper.processing.commons.configs.Configuration;
import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessage;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsRecord;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanUplinkMessage;
import org.iotmapper.processing.streams.mapping.IMapToRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapToLoraWanRecordType implements IMapToRecordType<LoRaWanMetricsRecord> {
    private static final Logger logger = LoggerFactory.getLogger(MapToLoraWanRecordType.class);
    private static MapToLoraWanRecordType singleton;

    private MapToLoraWanRecordType() {
    }

    public static MapToLoraWanRecordType get() {
        if (MapToLoraWanRecordType.singleton == null) {
            MapToLoraWanRecordType.singleton = new MapToLoraWanRecordType();
        }
        return MapToLoraWanRecordType.singleton;
    }

    @Override
    public Iterable<LoRaWanMetricsRecord> map(CygnusTopicMessage cygnusTopicMessage) {

        // ** Generic Settings
        double maxDistance = Configuration.LPWAN_MAX_DISTANCE;
        int detail = Configuration.LPWAN_GEOHASH_DETAIL;

        // ** LoraWan Settings
        maxDistance = Configuration.LORAWAN_MAX_DISTANCE;
        detail = Configuration.LORAWAN_GEOHASH_DETAIL;

        return LoRaWanUplinkMessage.create(cygnusTopicMessage, detail, maxDistance);
    }
}
