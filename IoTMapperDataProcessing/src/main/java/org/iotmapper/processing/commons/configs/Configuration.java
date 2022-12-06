package org.iotmapper.processing.commons.configs;

/**
 * Common location for Properties strings
 */
public class Configuration {

    // *** COMMON *** //

    // ** Behavior ** //
    public static final String IOTMAPPER_DROP_OLD_RECORDS_ENV_VAR = "IOTMAPPER_DROP_OLD_RECORDS";
    public static final String IOTMAPPER_DROP_OLD_RECORDS_DEFAULT = "true";
    public static boolean IOTMAPPER_DROP_OLD_RECORDS_VALUE = Boolean.parseBoolean(IOTMAPPER_DROP_OLD_RECORDS_DEFAULT);

    // * Kafka * //
    public static final String KAFKA_HOST_ENV_VAR = "KAFKA_HOST";
    public static final String KAFKA_ID_BASE_ENV_VAR = "KAFKA_ID_BASE";
    public static final String KAFKA_REQUEST_TIMEOUT_MS_CONFIG_ENV_VAR = "KAFKA_REQUEST_TIMEOUT_MS_CONFIG";
    public static final String KAFKA_RETRY_BACKOFF_MS_CONFIG_ENV_VAR = "KAFKA_RETRY_BACKOFF_MS_CONFIG";
    public static final String KAFKA_STATE_STORE_DIR_ENV_VAR = "KAFKA_STATE_STORE_DIR";
    // ** Kafka ** //
    public static final String KAFKA_ID_LORAWAN_ENV_VAR = "KAFKA_ID_LORAWAN";
    // ** TOPICS ** //
    public static final String LORAWAN_PARTITIONED_TOPIC_NAME = "LoRaWAN_Metrics";
    public static final String LORAWAN_AGGREGATION_TOPIC_NAME = "LoRaWAN_Aggregated_Metrics";
    /**
     * The state store name to use for LoRaWAN Gateway last updates
     */
    public static final String LORAWAN_GATEWAY_LOCATION_STATESTORE = "LoRaWan_GATEWAY_LOCATION_STATESTORE";
    /**
     * The state store name to use for LoRaWAN metric updates
     */
    public static final String LORAWAN_METRICS_STATESTORE = "LoRaWan_METRICS_STATESTORE";
    // ** SERVICE PATHS ** //
    public static final String LORAWAN_SERVICE = "lorawan";
    public static final String LORAWAN_SERVICE_PATH = "ttnmapper";



    // *** LORAWAN *** //
    // * Defaults * //
    public static final Double LORAWAN_MAX_DISTANCE = 100.0;
    public static final Integer LORAWAN_GEOHASH_DETAIL = 8;
    public static String KAFKA_HOST = "localhost:9092";
    public static String KAFKA_ID_BASE = "iotmapperdataprocessing";
    // ** STATE STORES ** //
    public static String KAFKA_REQUEST_TIMEOUT_MS_CONFIG = "20000";
    public static String KAFKA_RETRY_BACKOFF_MS_CONFIG = "500";
    public static String KAFKA_STATE_STORE_DIR = "/var/data/state-stores";
    // * Defaults * //
    public static Double LPWAN_MAX_DISTANCE = 100.0;
    public static Integer LPWAN_GEOHASH_DETAIL = 8;
    public static String KAFKA_ID_LORAWAN = "-lorawan";
}
