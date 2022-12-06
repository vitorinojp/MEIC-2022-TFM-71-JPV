package org.iotmapper.processing.models.metrics.lorawan;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.sql.Timestamp;

public class LoRaWanData {
    @JsonAlias("@type")
    public String type;
    public LoRaWanDevice.DeviceIds end_device_ids;
    public String[] correlation_ids;
    public Timestamp received_at;
    public LoRaWanUplinkMessage uplink_message;
    public LoRaWanSettings settings;
}
