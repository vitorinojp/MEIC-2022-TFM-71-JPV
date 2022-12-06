package org.iotmapper.processing.models.input.cygnus;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.Instant;

public class CygnusTopicMessageHeader {
    @JsonAlias("fiware-service")
    public String fiwareService;
    @JsonAlias("fiware-servicePath")
    public String fiwareServicePath;
    public Long timestamp;

    public CygnusTopicMessageHeader() {
    }

    public String getFiwareService() {
        return fiwareService;
    }

    public void setFiwareService(String fiwareService) {
        this.fiwareService = fiwareService;
    }

    public String getFiwareServicePath() {
        return fiwareServicePath;
    }

    public void setFiwareServicePath(String fiwareServicePath) {
        this.fiwareServicePath = fiwareServicePath;
    }

    public Instant getTimestamp() {
        if (timestamp != null) {
            return Instant.ofEpochMilli(timestamp);
        } else {
            return null;
        }
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp.getEpochSecond();
    }
}
