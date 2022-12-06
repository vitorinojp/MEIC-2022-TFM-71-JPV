package org.iotmapper.processing;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Patterns {

    @Test
    public void dmByServicePath() {
        Pattern pattern = Pattern.compile("(.*)xfffffx002f4(.*)");

        Matcher matcher = pattern.matcher("lorawanxfffffx002f4lora");
        assertTrue(matcher.matches());
    }
}
