package org.iotmapper.processing.commons.utils;

import org.iotmapper.processing.TestStrings;
import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessage;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

class JsonDeserializerTest {

    @Test
    public void TimestampTest() {
        long mili = 1640626762600L;
        String date = "2021-10-19T17:50:26.448748979Z";
        String json = "{ \"timestamp\": \"2021-10-19T17:50:26.448748979Z\"";
        String json2 = "{\"timestamp\":1640626762600}";

        Instant instant = Instant.parse(date);
        Timestamp timestamp = Timestamp.from(instant);
        Instant.ofEpochMilli(mili);

        JsonDeserializer<json> jsonDeserializer = new JsonDeserializer<>(JsonDeserializerTest.json.class, LoggerFactory.getLogger(JsonDeserializerTest.class));
        json jsonResukt = jsonDeserializer.deserialize("", json2.getBytes(StandardCharsets.UTF_8));

        JsonDeserializer<CygnusTopicMessage> jsonDeserializer2 = new JsonDeserializer<>(CygnusTopicMessage.class, LoggerFactory.getLogger(JsonDeserializerTest.class));
        CygnusTopicMessage cygnusTopicMessage = jsonDeserializer2.deserialize("", TestStrings.test00.getBytes(StandardCharsets.UTF_8));
    }

    public static class json {
        public Optional<String> test;
        public Optional<Long> timestamp;

        json() {

        }
    }
}