package org.iotmapper.processing.models.input.cygnus;

import com.fasterxml.jackson.annotation.JsonAlias;

public class CygnusTopicMessageBody {
    @JsonAlias("attributes")
    public CygnusTopicMessageAttribute[] attributes;
    public String type;
    public String id;

    public CygnusTopicMessageBody() {
    }

    public CygnusTopicMessageAttribute[] getAtributes() {
        return attributes;
    }

    public void setAtributes(CygnusTopicMessageAttribute[] atributes) {
        this.attributes = atributes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
