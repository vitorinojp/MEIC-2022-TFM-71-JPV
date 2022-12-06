package org.iotmapper.processing.models.metrics.lorawan;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.iotmapper.processing.lpwan.ILpwanMetrics;
import org.iotmapper.processing.lpwan.lorawan.LoRaWanMetrics;
import org.iotmapper.processing.models.metrics.CellIdGatewayIdPair;
import org.iotmapper.processing.models.metrics.GatewayLocation;
import org.iotmapper.processing.models.metrics.IMetricsRecord;

import java.time.Instant;

public class LoRaWanMetricsRecord implements IMetricsRecord {
    private LoRaWanMetrics metrics = null;
    private GatewayLocation gatewayLocation = null;
    private boolean gatewayLocationUpdate = false;
    private CellIdGatewayIdPair cellIdGatewayIdPair = null;
    private Instant timestamp = null;
    private Double maxDistance = 100.0;

    public LoRaWanMetricsRecord() {
    }

    public LoRaWanMetricsRecord(LoRaWanMetrics metrics, GatewayLocation gatewayLocation, CellIdGatewayIdPair cellIdGatewayIdPair, Instant timestamp, Double maxDistance) {
        this.metrics = metrics;
        this.gatewayLocation = gatewayLocation;
        this.cellIdGatewayIdPair = cellIdGatewayIdPair;
        this.timestamp = timestamp;
        this.maxDistance = maxDistance;
    }

    public LoRaWanMetrics getMetrics() {
        return this.metrics;
    }

    @Override
    public boolean hasGatewayLocationUpdate() {
        return this.gatewayLocationUpdate;
    }

    @Override
    public String getGatewayId() {
        return this.cellIdGatewayIdPair.getGatewayId();
    }

    @Override
    public GatewayLocation getGatewayLocation() {
        return this.gatewayLocation;
    }

    // TimeSTamp methods

    @JsonSetter("timestamp")
    public void setTimestampFromString(String timestamp) {
        this.timestamp = Instant.parse(timestamp);
    }

    @JsonGetter("timestamp")
    public String getTimestampAsString() {
        return this.timestamp.toString();
    }

    @Override
    public Instant getTimeStamp() {
        return this.timestamp;
    }

    @Override
    public CellIdGatewayIdPair getCellIdGatewayIdPair() {
        return this.cellIdGatewayIdPair;
    }

    @Override
    public ILpwanMetrics getIplwanMetrics() {
        return this.metrics;
    }

    @Override
    public Double getMaxDistance() {
        return this.maxDistance;
    }

    @Override
    public void setGatewayLocationUpdate(boolean gatewayLocationUpdate) {
        this.gatewayLocationUpdate = gatewayLocationUpdate;
    }

    public LoRaWanMetrics getLoRaWanMetrics() {
        return this.metrics;
    }

    @Override
    public String toString() {
        return "LoRaWanMetricsRecord{" +
                "metrics=" + metrics +
                ", gatewayLocation=" + gatewayLocation +
                ", gatewayLocationUpdate=" + gatewayLocationUpdate +
                ", cellIdGatewayIdPair=" + cellIdGatewayIdPair +
                ", timestamp=" + timestamp +
                ", maxDistance=" + maxDistance +
                '}';
    }
}
