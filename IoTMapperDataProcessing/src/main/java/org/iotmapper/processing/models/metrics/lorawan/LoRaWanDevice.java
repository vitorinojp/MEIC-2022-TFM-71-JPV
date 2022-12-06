package org.iotmapper.processing.models.metrics.lorawan;

public class LoRaWanDevice {
    private DeviceIds device_ids;

    public DeviceIds getDevice_ids() {
        return device_ids;
    }

    public void setDevice_ids(DeviceIds device_ids) {
        this.device_ids = device_ids;
    }

    public static class DeviceIds {
        public String device_id;
        public AppIds application_ids;
        public String dev_ui;
        public String dev_addr;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getDev_ui() {
            return dev_ui;
        }

        public void setDev_ui(String dev_ui) {
            this.dev_ui = dev_ui;
        }

        public String getDev_addr() {
            return dev_addr;
        }

        public void setDev_addr(String dev_addr) {
            this.dev_addr = dev_addr;
        }

        public AppIds getApplication_ids() {
            return application_ids;
        }

        public void setApplication_ids(AppIds application_ids) {
            this.application_ids = application_ids;
        }
    }

    public static class AppIds {
        public String application_ids;

        public String getApplication_ids() {
            return application_ids;
        }

        public void setApplication_ids(String application_ids) {
            this.application_ids = application_ids;
        }
    }
}
