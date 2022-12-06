package org.iotmapper.processing.models.input.cygnus;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class CygnusTopicMessageAttribute {
    public String name;
    public String type;
    public String value;
    public CygnusTopicMessageAttributesMetadata[] metadatas;

    public CygnusTopicMessageAttribute() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CygnusTopicMessageAttributesMetadata[] getMetadatas() {
        return metadatas;
    }

    public void setMetadatas(CygnusTopicMessageAttributesMetadata[] metadatas) {
        this.metadatas = metadatas;
    }

    public Timestamp getTimestamp() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

        if (Objects.equals(this.getName(), "TimeInstant")){
            try {
                return new Timestamp(dateformat.parse(this.getValue()).getTime());
            } catch (ParseException e) {
                // Suppress error to try metadata
            }
        }

        for (CygnusTopicMessageAttributesMetadata m : this.getMetadatas()) {
            if (Objects.equals(m.getName(), "TimeInstant")) {
                try {
                    return new Timestamp(dateformat.parse(m.getValue()).getTime());
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
