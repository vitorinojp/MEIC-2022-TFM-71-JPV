package org.iotmapper.processing.models.metrics;

import java.util.Objects;

/**
 * Generic holder of a Pair of Cell ID and Gateway ID identified by Strings
 */
public class CellIdGatewayIdPair {
    private String cellId;
    private String gatewayId;

    public CellIdGatewayIdPair() {
    }

    public CellIdGatewayIdPair(String cellId, String gatewayId) {
        this.cellId = cellId;
        this.gatewayId = gatewayId;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getIdString() {
        return this.gatewayId + ":" + this.cellId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellIdGatewayIdPair that = (CellIdGatewayIdPair) o;

        if (!Objects.equals(cellId, that.cellId)) return false;
        return Objects.equals(gatewayId, that.gatewayId);
    }

    @Override
    public int hashCode() {
        int result = cellId != null ? cellId.hashCode() : 0;
        result = 31 * result + (gatewayId != null ? gatewayId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"CellIdGatewayIdPair\", " +
                "\"cellId\":" + (cellId == null ? "null" : "\"" + cellId + "\"") + ", " +
                "\"gatewayId\":" + (gatewayId == null ? "null" : "\"" + gatewayId + "\"") +
                "}";
    }
}
