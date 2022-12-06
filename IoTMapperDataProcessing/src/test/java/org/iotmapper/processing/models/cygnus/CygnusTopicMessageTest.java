package org.iotmapper.processing.models.cygnus;

import org.iotmapper.processing.TestStrings;
import org.iotmapper.processing.commons.utils.JsonDeserializer;
import org.iotmapper.processing.models.input.cygnus.CygnusTopicMessage;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CygnusTopicMessageTest {
    private Logger logger = LoggerFactory.getLogger(CygnusTopicMessageTest.class);

    @Test
    public void parseJsonString() {
        JsonDeserializer<CygnusTopicMessage> jsonDeserializer = new JsonDeserializer<>(CygnusTopicMessage.class, logger);
        CygnusTopicMessage test00_object = jsonDeserializer.deserialize("", TestStrings.test03.getBytes(StandardCharsets.UTF_8));

        assertNotNull(test00_object);
        assertEquals("lorawan", test00_object.getService());
        assertEquals("/ttnmapper", test00_object.getServicePath());
        assertNotNull(test00_object.getTimestamp());
        assertNotNull(test00_object.getAtributes()[0]);
        assertEquals("rawMessage", test00_object.getAtributes()[1].getName());
        //assertEquals(TestStrings.test00_value, test00_object.getAtributes()[1].getValue());
        assertNotNull(test00_object.getAtributes()[0].getMetadatas());
        assertNotNull(test00_object.getAtributes()[0].getTimestamp());
        assertEquals("RawMessage", test00_object.getType());

    }
}