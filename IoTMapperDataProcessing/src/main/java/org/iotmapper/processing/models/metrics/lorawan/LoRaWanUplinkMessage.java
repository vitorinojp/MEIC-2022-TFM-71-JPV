package org.iotmapper.processing.models.metrics.lorawan;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.iotmapper.processing.commons.utils.GeoTools;
import org.iotmapper.processing.commons.utils.JsonDeserializer;
import org.iotmapper.processing.lpwan.lorawan.LoRaWanMetrics;
import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessage;
import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessageAttribute;
import org.iotmapper.processing.models.metrics.CellIdGatewayIdPair;
import org.iotmapper.processing.models.metrics.GatewayLocation;
import org.iotmapper.processing.models.metrics.IMetricsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

public class LoRaWanUplinkMessage implements IMetricsMessage {
    private static Logger logger = LoggerFactory.getLogger(LoRaWanUplinkMessage.class);
    public int f_port;
    public int f_cnt;
    @JsonAlias("decoded_payload")
    public DecodedPayload location;
    public LoRaWanGatewayInfo[] rx_metadata;
    public String received_at;

    /**
     * Deserializes the CygnusTopicMessage, assuming it's a LoRaWanUplinkMessage, check if it's valid and returns an Iterable split by gateway
     *
     * @param cygnusTopicMessage - message received to deserialize and split
     * @param detail             - level of detail requested in the transformation, see: utils.GeoTools
     * @param maxDistance        - max distance to consider for future changes
     * @return Iterable<IMetricsRecord> with zero or more LoRaWanMetricsRecord
     */
    public static Iterable<LoRaWanMetricsRecord> create(CygnusTopicMessage cygnusTopicMessage, int detail, Double maxDistance) {
        Optional<CygnusTopicMessageAttribute> atribute = Arrays.stream(cygnusTopicMessage.getAtributes()).filter((atr) -> Objects.equals(atr.name, "rawMessage")).findFirst();
        if (atribute.isPresent()) {
            String value = atribute.get().value;
            String json = URLDecoder.decode(value, StandardCharsets.UTF_8);
            JsonDeserializer<LoRaWanUplinkMessage> jsonDeserializer = new JsonDeserializer<>(LoRaWanUplinkMessage.class, LoggerFactory.getLogger(LoRaWanUplinkMessage.class));
            LoRaWanUplinkMessage records = jsonDeserializer.deserialize("", json.getBytes(StandardCharsets.UTF_8));
            if (records.isValid()) {
                return records.splitGateways(cygnusTopicMessage.getTimestamp(), detail, maxDistance);
            }
            logger.info("Invalid message received, will be discarded.");
        }
        logger.info("Empty message received (no \"rawMessage\" field in attributes? (atribute == null): " + (atribute.isEmpty()) + " ), nothing to pass to next step.");
        // Empty list
        return new ArrayList<>();
    }

    /**
     * Splits the current message by gateway
     *
     * @param detail      - level of detail requested in the transformation, see: utils.GeoTools
     * @param maxDistance - max distance to consider for future changes
     * @return Iterable<IMetricsRecord> with one or more LoRaWanMetricsRecord
     */
    private Iterable<LoRaWanMetricsRecord> splitGateways(Instant cygnus_timestamp, int detail, Double maxDistance) {
        ArrayList<LoRaWanMetricsRecord> listMetricsRecords = new ArrayList<>();
        HashMap<String, GatewayLocation> findMessagesFromSameGw = new HashMap<>();

        for (LoRaWanGatewayInfo gw : rx_metadata) {
            // Attribute a timestamp by best effort
            Instant timestamp;
            // Ideal is if timestamp was inserted by the gateway
            if (gw.getTime() != null) {
                timestamp = gw.getTime();
            }
            // if doesn't exist use the network server timestamp
            else if (this.received_at != null) {
                timestamp = Instant.parse(received_at);
            }
            // if neither exists use cygnus timestamp
            else if (cygnus_timestamp != null) {
                timestamp = cygnus_timestamp;
            }
            // Last resort as to not discard the message if the measures are not malformed
            else {
                timestamp = Instant.now();
            }

            LoRaWanMetrics loRaWanMetrics = new LoRaWanMetrics(gw.getRssi(), gw.getSignal_rssi(), gw.getChannel_rssi(), gw.getRssi_standard_deviation(), gw.getSnr());
            GatewayLocation gatewayLocation = new GatewayLocation(timestamp, gw.getLocationPoint(), gw.getAltitude());
            CellIdGatewayIdPair cellIdGatewayIdPair = new CellIdGatewayIdPair(GeoTools.getGeoHash(location.latitude, location.getLongitude(), detail), gw.getGateway_ids());

            GatewayLocation previous = findMessagesFromSameGw.get(gw.getGateway_ids());
            if (previous == null){
                findMessagesFromSameGw.put(gw.getGateway_ids(), gatewayLocation);
            } else {
                gatewayLocation = previous;
            }

            LoRaWanMetricsRecord record = new LoRaWanMetricsRecord(
                    loRaWanMetrics,
                    gatewayLocation,
                    cellIdGatewayIdPair,
                    timestamp,
                    maxDistance
            );

            listMetricsRecords.add(record);
        }

        return listMetricsRecords;
    }

    public int getF_port() {
        return f_port;
    }

    public void setF_port(int f_port) {
        this.f_port = f_port;
    }

    public int getF_cnt() {
        return f_cnt;
    }

    public void setF_cnt(int f_cnt) {
        this.f_cnt = f_cnt;
    }

    public DecodedPayload getLocation() {
        return location;
    }

    public void setLocation(DecodedPayload location) {
        this.location = location;
    }

    public LoRaWanGatewayInfo[] getRx_metadata() {
        return rx_metadata;
    }

    public void setRx_metadata(LoRaWanGatewayInfo[] rx_metadata) {
        this.rx_metadata = rx_metadata;
    }

    @Override
    public boolean isValid() {
        // At least the location must exist
        if (this.location != null && this.location.getLongitude() != null && this.location.getLatitude() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static class DecodedPayload {
        @JsonAlias("alt")
        public Integer altitude;
        @JsonAlias("lon")
        public Double longitude;
        @JsonAlias("lat")
        public Double latitude;

        public Integer getAltitude() {
            return altitude;
        }

        public void setAltitude(Integer altitude) {
            this.altitude = altitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
    }
}
