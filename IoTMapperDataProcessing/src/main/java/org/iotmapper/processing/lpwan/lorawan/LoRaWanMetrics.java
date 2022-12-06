package org.iotmapper.processing.lpwan.lorawan;

import org.iotmapper.processing.lpwan.ILpwanMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoRaWanMetrics implements ILpwanMetrics {
    private final Logger logger = LoggerFactory.getLogger(LoRaWanMetrics.class);

    private Optional<Double> rssi;
    private Optional<Double> signal_rssi;
    private Optional<Double> channel_rssi;
    private Optional<Double> rssi_standard_deviation;
    private Optional<Double> snr;
    private int rssi_count = 1;
    private int signal_rssi_count = 1;
    private int channel_rssi_count = 1;
    private int rssi_standard_deviation_count = 1;
    private int snr_count = 1;

    public LoRaWanMetrics(Double rssi, Double signal_rssi, Double channel_rssi, Double rssi_standard_deviation, Double snr) {
        this.rssi = Optional.of(rssi);
        this.signal_rssi = Optional.of(signal_rssi);
        this.channel_rssi = Optional.of(channel_rssi);
        this.rssi_standard_deviation = Optional.of(rssi_standard_deviation);
        this.snr = Optional.of(snr);
    }

    public LoRaWanMetrics() {
        this.rssi = Optional.empty();
        this.signal_rssi = Optional.empty();
        this.channel_rssi = Optional.empty();
        this.rssi_standard_deviation = Optional.empty();
        this.snr = Optional.empty();
    }

    public void add(LoRaWanMetrics loRaWanMetrics) {
        loRaWanMetrics.rssi.ifPresent(
                (newValue) ->
                        this.rssi = this.rssi.map(
                                (val) -> addToAverageLog10(val, rssi_count++, newValue)
                        )
        );
        loRaWanMetrics.signal_rssi.ifPresent(
                (newValue) ->
                        this.signal_rssi = this.signal_rssi.map(
                                (val) -> addToAverageLog10(val, signal_rssi_count++, newValue)
                        )
        );
        loRaWanMetrics.channel_rssi.ifPresent(
                (newValue) ->
                        this.channel_rssi = this.channel_rssi.map(
                                (val) -> addToAverageLog10(val, channel_rssi_count++, newValue)
                        )
        );
        loRaWanMetrics.rssi_standard_deviation.ifPresent(
                (newValue) ->
                        this.rssi_standard_deviation = this.rssi_standard_deviation.map(
                                (val) -> addToAverageLog10(val, rssi_standard_deviation_count++, newValue)
                        )
        );
        loRaWanMetrics.snr.ifPresent(
                (newValue) ->
                        this.snr = this.snr.map(
                                (val) -> addToAverageLog10(val, snr_count++, newValue)
                        )
        );
    }

    public void sub(LoRaWanMetrics loRaWanMetrics) {
        loRaWanMetrics.rssi.ifPresent(
                (newValue) ->
                        this.rssi = this.rssi.map((val) -> subtractFromAverage(val, rssi_count--, newValue)
                        )
        );
        loRaWanMetrics.signal_rssi.ifPresent(
                (newValue) ->
                        this.signal_rssi = this.signal_rssi.map(
                                (val) -> subtractFromAverage(val, signal_rssi_count--, newValue)
                        )
        );
        loRaWanMetrics.channel_rssi.ifPresent(
                (newValue) ->
                        this.channel_rssi = this.channel_rssi.map(
                                (val) -> subtractFromAverage(val, channel_rssi_count--, newValue)
                        )
        );
        loRaWanMetrics.rssi_standard_deviation.ifPresent(
                (newValue) ->
                        this.rssi_standard_deviation = this.rssi_standard_deviation.map((val) -> subtractFromAverage(val, rssi_standard_deviation_count--, newValue)
                        )
        );
        loRaWanMetrics.snr.ifPresent(
                (newValue) ->
                        this.snr = this.snr.map((val) -> subtractFromAverage(val, snr_count--, newValue)
                        )
        );
    }

    /**
     * Adds a log10 value toa a log10 mean by converting both number to
     *
     * @param averageLog current mean for a log10 measure
     * @param size       number of values that compose the log10 mean
     * @param valueLog   log10 value to add
     * @return the new mean value for the measure
     */
    private double addToAverageLog10(double averageLog, int size, double valueLog) {
        double average = Math.pow(10, averageLog);
        double value = Math.pow(10, valueLog);

        return Math.log10((size * average + value) / (size + 1));
    }

    private double subtractFromAverage(double averageLog, int size, double valueLog) {
        double average = Math.pow(10, averageLog);
        double value = Math.pow(10, valueLog);

        if (size == 1) return 0;       // wrong but then adding a value "works"
        // if (size == 1) return NAN;     // mathematically proper
        // assert(size > 1);              // debug-mode check
        if (size <= 0) return 0;        // always check
        return Math.log10((size * average - value) / (size - 1));
    }

    public Optional<Double> getRssi() {
        return rssi;
    }

    public int getRssi_count() {
        return rssi_count;
    }

    public void setRssi_count(int rssi_count) {
        this.rssi_count = rssi_count;
    }

    public Optional<Double> getSignal_rssi() {
        return signal_rssi;
    }

    public int getSignal_rssi_count() {
        return signal_rssi_count;
    }

    public void setSignal_rssi_count(int signal_rssi_count) {
        this.signal_rssi_count = signal_rssi_count;
    }

    public Optional<Double> getChannel_rssi() {
        return channel_rssi;
    }

    public int getChannel_rssi_count() {
        return channel_rssi_count;
    }

    public void setChannel_rssi_count(int channel_rssi_count) {
        this.channel_rssi_count = channel_rssi_count;
    }

    public Optional<Double> getRssi_standard_deviation() {
        return rssi_standard_deviation;
    }

    public int getRssi_standard_deviation_count() {
        return rssi_standard_deviation_count;
    }

    public void setRssi_standard_deviation_count(int rssi_standard_deviation_count) {
        this.rssi_standard_deviation_count = rssi_standard_deviation_count;
    }

    public Optional<Double> getSnr() {
        return snr;
    }

    public int getSnr_count() {
        return snr_count;
    }

    public void setSnr_count(int snr_count) {
        this.snr_count = snr_count;
    }

    @Override
    public String toString() {
        return "{\"name\": \"rssi\", \"value\": " + rssi.get() + ", \"count\":" + rssi_count + "}," +
                "{\"name\": \"signal_rssi\", \"value\": " + signal_rssi.get() + ", \"count\":" + signal_rssi_count + "}," +
                "{\"name\": \"channel_rssi\", \"value\": " + channel_rssi.get() + ", \"count\":" + channel_rssi_count + "}," +
                "{\"name\": \"rssi_standard_deviation\", \"value\": " + rssi_standard_deviation.get() + ", \"count\":" + rssi_standard_deviation_count + "}," +
                "{\"name\": \"snr\", \"value\": " + snr.get() + ", \"count\":" + snr_count + "}";
    }
}
