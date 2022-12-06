package org.iotmapper.processing.models.metrics;

/**
 * Result of reducing (aggregating)
 */
public interface IMetricsAggregate<R extends IMetricsRecord> {
    /**
     * Initial value for aggregation.
     * If not implemented  will return null
     *
     * @return an instance of the interface with an initial value or null
     */
    static IMetricsAggregate initialValue() {
        return null;
    }

    /**
     * Add the MetricsRecord to the aggregate
     *
     * @param add - the MetricsRecord value to add
     * @return the current MetricsAggregateResult
     */
    IMetricsAggregate add(R add);

    /**
     * Remove the MetricsRecord from tha aggregate
     *
     * @param sub - the MetricsRecord value to remove
     * @return the current MetricsAggregateResult
     */
    IMetricsAggregate subtract(R sub);

    /**
     * Convert to string representation
     *
     * @return the JSON string that represents a MeasuresAggregate
     */
    String getMeasures();

    /**
     * Timestamp of the last action that led to the rest of the this aggregation
     *
     * @return
     */
    String getDateLastGwUpdate();

    /**
     * Timestamp of the last measure aggregated
     *
     * @return
     */
    String getDateLastUpdate();
}
