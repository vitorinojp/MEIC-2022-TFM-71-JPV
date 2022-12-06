package org.iotmapper.processing.models.metrics;

import org.geojson.Point;
import org.iotmapper.processing.commons.utils.GeoTools;

import java.time.Instant;

public class GatewayLocation {
    private Instant date;
    private Point point;
    private Double alt;


    public GatewayLocation() {
    }

    /**
     * Create a point, if parameters don't exist they must be supplied as null
     *
     * @param date
     * @param point
     * @param alt
     */
    public GatewayLocation(Instant date, Point point, Double alt) {
        this.date = date;
        this.point = point;
        this.alt = alt;
    }

    /**
     * Whether there is an org.geojson.Point inside
     *
     * @return true if yes, false if not
     */
    public boolean isEmpty() {
        return point == null;
    }

    public Instant getTimestamp() {
        return date;
    }

    public void setTimestamp(Instant date) {
        this.date = date;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Calls GeoTools.distance(this, gatewayLocation) to assert distance, deemed incomparable It's considered to be always true
     *
     * @param gatewayLocation - location to compare too
     * @param i               - max distance
     * @return true if more than i, or incomparable, false id less than i
     */
    public boolean distanceMoreThan(GatewayLocation gatewayLocation, Double i) {
        Double distance = GeoTools.distance(this, gatewayLocation);
        if (distance == null) {
            return true;
        } else {
            return distance > i;
        }
    }

}
