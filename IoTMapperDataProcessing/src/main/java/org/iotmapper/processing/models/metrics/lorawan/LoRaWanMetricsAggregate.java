package org.iotmapper.processing.models.metrics.lorawan;

import org.iotmapper.processing.IoTMapperDataProcessing;
import org.iotmapper.processing.lpwan.lorawan.LoRaWanMetrics;
import org.iotmapper.processing.models.metrics.IMetricsAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class LoRaWanMetricsAggregate implements IMetricsAggregate<LoRaWanMetricsRecord> {
    private Logger logger;

    private LoRaWanMetrics loRaWanMetrics;
    private Instant dateLastGwUpdate;
    private Instant dateLastUpdate;


    public LoRaWanMetricsAggregate() {
        this.logger = LoggerFactory.getLogger(IoTMapperDataProcessing.class);
    }

    public LoRaWanMetricsAggregate(LoRaWanMetrics loRaWanMetrics) {
        this.loRaWanMetrics = loRaWanMetrics;
    }

    public static LoRaWanMetricsAggregate initialValue() {
        return new LoRaWanMetricsAggregate();
    }

    public LoRaWanMetrics getLoRaWanMetrics() {
        return loRaWanMetrics;
    }

    public void setLoRaWanMetrics(LoRaWanMetrics loRaWanMetrics) {
        this.loRaWanMetrics = loRaWanMetrics;
    }

    public LoRaWanMetricsAggregate add(LoRaWanMetrics add) {
        if (this.loRaWanMetrics == null) {
            this.loRaWanMetrics = add;
        } else {
            this.loRaWanMetrics.add(add);
        }
        return this;
    }

    public LoRaWanMetricsAggregate subtract(LoRaWanMetrics sub) {
        if (this.loRaWanMetrics == null) {
            this.loRaWanMetrics = new LoRaWanMetrics();
        } else {
            this.loRaWanMetrics.sub(sub);
        }
        return this;
    }

    @Override
    public LoRaWanMetricsAggregate add(LoRaWanMetricsRecord add) {
        this.dateLastUpdate = add.getTimeStamp();
        if (this.dateLastGwUpdate == null)
            this.dateLastGwUpdate = add.getTimeStamp();
        return this.add(add.getMetrics());
    }

    @Override
    public LoRaWanMetricsAggregate subtract(LoRaWanMetricsRecord sub) {
        this.dateLastUpdate = sub.getTimeStamp();
        if (this.dateLastGwUpdate == null)
            this.dateLastGwUpdate = sub.getTimeStamp();
        return this.subtract(sub.getMetrics());
    }

    @Override
    public String getMeasures() {
        return this.loRaWanMetrics.toString();
    }

    @Override
    public String getDateLastGwUpdate() {
        return this.dateLastGwUpdate.toString();
    }

    @Override
    public String getDateLastUpdate() {
        return this.dateLastUpdate.toString();
    }


    @Override
    public String toString() {
        return "LoRaWanMetricsAggregate{" +
                "loRaWanMetrics=" + loRaWanMetrics +
                ", dateLastGwUpdate=" + dateLastGwUpdate +
                ", dateLastUpdate=" + dateLastUpdate +
                '}';
    }
}
