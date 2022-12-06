package org.iotmapper.processing.streams.mapping;

import org.iotmapper.processing.TestStrings;
import org.iotmapper.processing.commons.utils.JsonDeserializer;
import org.iotmapper.processing.lpwan.lorawan.LoRaWanMetrics;
import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessage;
import org.iotmapper.processing.models.metrics.lorawan.LoRaWanMetricsRecord;
import org.iotmapper.processing.streams.lpwan.lorawan.MapToLoraWanRecordType;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class MapToRecordTypeTest {
    CygnusTopicMessage cygnusTopicMessage_00;
    Properties properties = new Properties();

    {

        properties.put("LoRaWAN_SERVICE", "ttnmapper");

        JsonDeserializer<CygnusTopicMessage> jsonDeserializer = new JsonDeserializer<>(CygnusTopicMessage.class, null);
        cygnusTopicMessage_00 = jsonDeserializer.deserialize("", TestStrings.test00.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void mapLoRaWanTest() {
        Iterable<LoRaWanMetricsRecord> records = MapToLoraWanRecordType.get().map(this.cygnusTopicMessage_00);

        AtomicInteger count = new AtomicInteger();
        records.forEach((record) -> {
            count.getAndIncrement();

            assertNull(record.getGatewayLocation().getPoint());
            assertEquals("test", record.getGatewayId());
            assertNotNull(record.getCellIdGatewayIdPair());
            assertNotNull(record.getCellIdGatewayIdPair().getCellId());
            assertNotNull(((LoRaWanMetrics) record.getIplwanMetrics()).getRssi().get());
            assertNotNull(((LoRaWanMetrics) record.getIplwanMetrics()).getChannel_rssi().get());
            assertNotNull(((LoRaWanMetrics) record.getIplwanMetrics()).getSignal_rssi().get());
            assertNotNull(((LoRaWanMetrics) record.getIplwanMetrics()).getSnr().get());
            assertNotNull(((LoRaWanMetrics) record.getIplwanMetrics()).getRssi_standard_deviation().get());
        });
        assertEquals(1, count.get());
    }
}