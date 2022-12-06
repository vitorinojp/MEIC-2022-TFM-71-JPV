package org.iotmapper.processing.models.input.cygnus;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.Instant;

public class CygnusTopicMessage {
    @JsonAlias("headers")
    public CygnusTopicMessageHeader[] headers;
    @JsonAlias("body")
    public CygnusTopicMessageBody body;

    public CygnusTopicMessage() {
    }

    public String getService() {
        for (CygnusTopicMessageHeader h : headers) {
            if (h.getFiwareService() != null) return h.getFiwareService();
        }
        return null;
    }

    public String getServicePath() {
        for (CygnusTopicMessageHeader h : headers) {
            if (h.getFiwareServicePath() != null) return h.getFiwareServicePath();
        }
        return null;
    }

    public Instant getTimestamp() {
        for (CygnusTopicMessageHeader h : headers) {
            if (h.getTimestamp() != null) return h.getTimestamp();
        }
        return null;
    }

    public CygnusTopicMessageAttribute[] getAtributes() {
        return body.getAtributes();
    }

    public String getType() {
        return body.getType();
    }

    public String getId() {
        return body.getId();
    }
}
