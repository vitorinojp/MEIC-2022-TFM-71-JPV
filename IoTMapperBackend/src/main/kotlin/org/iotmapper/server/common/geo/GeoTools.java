package org.iotmapper.server.common.geo;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.io.GeohashUtils;
import org.locationtech.spatial4j.shape.Point;

public class GeoTools {
    private static final SpatialContext geo = SpatialContext.GEO;

    public static String getGeoHash(double lat, double lon, int precision) {
        return GeohashUtils.encodeLatLon(lat, lon, precision);
    }

    public static GeoPair decodeGeoHash(String geohash) {
        Point point = GeohashUtils.decode(geohash, geo);
        return new GeoPair(point.getLat(), point.getLon());
    }

    public static class GeoPair {
        private Double lat;
        private Double lon;

        public GeoPair(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public GeoPair() {

        }

        public boolean isEmpty() {
            return (this.lat == null || this.lon == null);
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}
