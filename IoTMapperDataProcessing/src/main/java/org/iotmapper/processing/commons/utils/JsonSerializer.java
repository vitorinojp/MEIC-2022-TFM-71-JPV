package org.iotmapper.processing.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * A Serializer for Kafka Streams using Jackson
 * Adapted from Bill Bejeck (https://github.com/bbejeck/kafka-streams/blob/master/src/main/java/bbejeck/serializer/JsonSerializer.java) to use Jackson in place of Gson,
 * to get support for multiple formats with a single dependency.
 *
 * @param <T>
 */
public class JsonSerializer<T> implements Serializer<T> {

    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
            .registerModule(new Jdk8Module())
            .registerModule(new JSR310Module());
    private Logger logger = LoggerFactory.getLogger(JsonDeserializer.class);

    public JsonSerializer() {
    }

    public JsonSerializer(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String topic, T t) {
        if (t == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            logger.error("On {}. Error serializing object to JSON for topic {}, with JsonProcessingException: {}", JsonSerializer.class, topic, e.toString());
        }

        return null;
    }

    @Override
    public void close() {

    }
}
