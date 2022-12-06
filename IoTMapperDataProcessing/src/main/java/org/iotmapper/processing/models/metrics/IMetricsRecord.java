package org.iotmapper.processing.models.metrics;

import org.iotmapper.processing.lpwan.ILpwanMetrics;

import java.time.Instant;

/**
 * Generic representation of a record (set of received metrics) for a <Gateway, Cell> pair in a message.
 */
public interface IMetricsRecord {
    /**
     * Whether the record contains a change in location for
     *
     * @return true if yes, false f not
     */
    boolean hasGatewayLocationUpdate();

    /**
     * Get the Gateway Id
     *
     * @return String with the Gateway Id;
     */
    String getGatewayId();

    /**
     * Get the location of the gateway, must be called after checkGatewayChange() and hasGatewayLocationUpdate()
     *
     * @return the location of the gateway
     */
    GatewayLocation getGatewayLocation();

    /**
     * Implementation dependent instant that the metrics of the record where registered
     *
     * @return the java.sql.Timestamp object
     */
    Instant getTimeStamp();

    /**
     * Gets the identifier of the pair of Cell ID and Gateway ID for Group By purposes
     *
     * @return the CellIdGatewayIdPair associated with the MetricsRecord
     */
    CellIdGatewayIdPair getCellIdGatewayIdPair();

    /**
     * Gets the object with the implementation specific metrics object for aggregation
     *
     * @return ILpwanMetrics - with the associated metrics
     */
    ILpwanMetrics getIplwanMetrics();

    /**
     * Set up max distance
     *
     * @return Double with the max distance to consider in gateway changes
     */
    Double getMaxDistance();

    /**
     * Set whether the gateway changed in this message
     *
     * @param gatewayLocationUpdate - true if changed, false otherwise
     */
    void setGatewayLocationUpdate(boolean gatewayLocationUpdate);
}
