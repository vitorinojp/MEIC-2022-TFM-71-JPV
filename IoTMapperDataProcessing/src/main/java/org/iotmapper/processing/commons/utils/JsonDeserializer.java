package org.iotmapper.processing.commons.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.apache.kafka.common.serialization.Deserializer;
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
public class JsonDeserializer<T> implements Deserializer<T> {

    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
            .registerModule(new Jdk8Module())
            .registerModule(new JSR310Module());
    private Logger logger = LoggerFactory.getLogger(JsonDeserializer.class);
    private Class<T> deserializedClass;

    public JsonDeserializer(Class<T> deserializedClass) {
        this.deserializedClass = deserializedClass;
    }

    public JsonDeserializer(Class<T> deserializedClass, Logger logger) {
        this.deserializedClass = deserializedClass;
        this.logger = logger;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(Map<String, ?> map, boolean b) {
        if (deserializedClass == null) {
            deserializedClass = (Class<T>) map.get("serializedClass");
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        try {
            return objectMapper.readValue(bytes, deserializedClass);
        } catch (Exception e) {
            logger.error("On {}. Error deserializing JSON to {}, with JsonProcessingException: {}", JsonDeserializer.class, deserializedClass, e.toString());
        }

        return null;
    }

    @Override
    public void close() {

    }
}