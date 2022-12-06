package org.iotmapper.processing.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.iotmapper.processing.models.metrics.GatewayLocation;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.io.GeohashUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeFactory;

public class GeoTools {
    private static SpatialContext geo = SpatialContext.GEO;

    /**
     * If gw1 and gw2 are both isEmpty() then return 0.0
     * If one is and the other isn't then return null
     * else return distance calc
     *
     * @param gw1
     * @param gw2
     * @return double or null
     */
    public static Double distance(GatewayLocation gw1, GatewayLocation gw2) {
        // check if its a valid location
        boolean gw1Empty = gw1.isEmpty();
        boolean gw2Empty = gw2.isEmpty();

        if (gw1Empty && gw2Empty) {
            return 0.0;
        } else if (gw1Empty || gw2Empty) {
            return null;
        } else {
            ShapeFactory shapeFactory = geo.getShapeFactory();
            Point point1 = shapeFactory.pointLatLon(
                    gw1.getPoint().getCoordinates().getLatitude(),
                    gw1.getPoint().getCoordinates().getLongitude()

            );
            Point point2 = shapeFactory.pointLatLon(
                    gw2.getPoint().getCoordinates().getLatitude(),
                    gw2.getPoint().getCoordinates().getLongitude()

            );
            return geo.calcDistance(point1, point2);
        }
    }

    public static String getGeoHash(double lat, double lon, int precision) {
        return GeohashUtils.encodeLatLon(lat, lon, precision);
    }

    public static GeoPair decodeGeoHash(String geohash) {
        Point point = GeohashUtils.decode(geohash, geo);
        return new GeoPair(point.getLat(), point.getLon());
    }

    public static class GeoPair {
        private double lat;
        private double lon;

        public GeoPair(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
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

        public String toGeoJson() throws JsonProcessingException {
            org.geojson.Point point = new org.geojson.Point(this.lon, this.lat);
            Feature feature = new Feature();
            feature.setGeometry(point);
            return new ObjectMapper().writeValueAsString(feature);
        }
    }
}
