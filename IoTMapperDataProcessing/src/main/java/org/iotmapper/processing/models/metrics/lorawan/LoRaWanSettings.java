package org.iotmapper.processing.models.metrics.lorawan;

public class LoRaWanSettings {
    public DataRate data_rate;
    public String coding_rate;
    public String frequency;

    public static class DataRate {
        public Lora lora;
    }

    public static class Lora {
        public int bandwidth;
        public int spreading_factor;
    }
}
