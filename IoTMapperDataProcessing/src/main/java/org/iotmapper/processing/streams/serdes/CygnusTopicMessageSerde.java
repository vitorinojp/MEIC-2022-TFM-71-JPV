package org.iotmapper.processing.streams.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.iotmapper.processing.commons.utils.JsonDeserializer;
import org.iotmapper.processing.commons.utils.JsonSerializer;
import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessage;

import java.util.Map;

public class CygnusTopicMessageSerde implements Serde<CygnusTopicMessage> {

    public CygnusTopicMessageSerde() {
    }

    @Override
    public Serializer<CygnusTopicMessage> serializer() {
        return new CygnusTopicMessageSerializer();
    }

    @Override
    public Deserializer<CygnusTopicMessage> deserializer() {
        return new CygnusTopicMessageDesirializer();
    }

    public static class CygnusTopicMessageSerializer implements Serializer<CygnusTopicMessage> {
        private static final JsonSerializer<CygnusTopicMessage> jsonSerializer = new JsonSerializer<>();

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
            Serializer.super.configure(configs, isKey);
        }

        @Override
        public byte[] serialize(String s, CygnusTopicMessage cygnusTopicMessage) {
            return jsonSerializer.serialize(s, cygnusTopicMessage);
        }
    }

    public static class CygnusTopicMessageDesirializer implements Deserializer<CygnusTopicMessage> {
        private static final JsonDeserializer<CygnusTopicMessage> jsonDeserializer = new JsonDeserializer<>(CygnusTopicMessage.class);

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
            Deserializer.super.configure(configs, isKey);
        }

        @Override
        public CygnusTopicMessage deserialize(String s, byte[] bytes) {
            return jsonDeserializer.deserialize(s, bytes);
        }
    }
}
