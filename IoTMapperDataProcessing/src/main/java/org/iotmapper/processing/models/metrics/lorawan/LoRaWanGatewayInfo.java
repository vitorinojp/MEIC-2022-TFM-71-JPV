package org.iotmapper.processing.models.metrics.lorawan;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.geojson.Point;

import java.time.Instant;

public class LoRaWanGatewayInfo {
    public GatewayIds gateway_ids;
    public PacketBroker packet_broker;
    @JsonAlias({"date"})
    public String time;
    public long timestamp;
    public double rssi;
    public double signal_rssi;
    public double channel_rssi;
    public double snr;
    public double rssi_standard_deviation;
    public Location location;

    public String getGateway_ids() {
        if (packet_broker == null){
            return gateway_ids.gateway_id;
        } else {
            return packet_broker.forwarder_net_id +
                    "_" + packet_broker.forwarder_tenant_id +
                    "_" + packet_broker.forwarder_cluster_id +
                    "_" + packet_broker.forwarder_gateway_id;
        }
    }

    public PacketBroker getPacket_broker() {
        return packet_broker;
    }

    public Instant getTime() {
        if (time != null) {
            return Instant.parse(time);
        } else {
            return null;
        }
    }

    public double getRssi() {
        return rssi;
    }

    public double getSignal_rssi() {
        return signal_rssi;
    }

    public double getChannel_rssi() {
        return channel_rssi;
    }

    public double getSnr() {
        return snr;
    }

    public double getRssi_standard_deviation() {
        return rssi_standard_deviation;
    }

    public Point getLocationPoint() {
        if (location != null && location.longitude != null && location.latitude != null) {
            return new Point(location.longitude, location.latitude);
        } else {
            return null;
        }
    }

    public Double getAltitude() {
        if (location != null) {
            return location.altitude;
        } else {
            return null;
        }
    }

    public static class GatewayIds {
        public String gateway_id;
        public String eui;
    }

    public static class PacketBroker {
        public PacketBroker() {}

        public String message_id;
        public String forwarder_net_id;
        public String forwarder_tenant_id;
        public String forwarder_cluster_id;
        public String forwarder_gateway_id;
        public String home_network_net_id;
        public String home_network_tenant_id;
        public String home_network_cluster_id;

    }

    public static class Location {
        public Location() {}

        @JsonAlias({"alt", "altitude"})
        public Double altitude;
        @JsonAlias({"lon", "longitude"})
        public Double longitude;
        @JsonAlias({"lat", "latitude"})
        public Double latitude;
        public String source;
    }
}
