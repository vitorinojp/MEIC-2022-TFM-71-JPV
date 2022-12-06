package org.iotmapper.processing;

import org.apache.commons.cli.*;
import org.iotmapper.processing.commons.StreamProcessingJob;
import org.iotmapper.processing.commons.configs.Configuration;
import org.iotmapper.processing.streams.lpwan.lorawan.LoRaWanTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class IoTMapperDataProcessing {
    private static Logger logger;

    public static void main(String[] args) {
        // *** Logging *** //
        logger = LoggerFactory.getLogger(IoTMapperDataProcessing.class);

        // *** Configurations Loading *** //
        try {
            getCmdOptions(args);
            getEnvVars();
        } catch (ParseException e) {
            logger.error("Failed to load options with ParseException: {}", e.toString());
        }


        // *** Setup jobs *** //
        ArrayList<StreamProcessingJob> streams = new ArrayList<>();

        LoRaWanTopology loRaWanTopology = new LoRaWanTopology();
        streams.add(new StreamProcessingJob(
                loRaWanTopology,
                loRaWanTopology.getKafkaStreamsConfig(),
                logger
        ));


        // *** RUNTIME *** //

        final CountDownLatch latch = new CountDownLatch(streams.size());

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-wordcount-shutdown-hook") {
            @Override
            public void run() {
                for (StreamProcessingJob job : streams) {
                    job.stop();
                }
                latch.countDown();
            }
        });

        try {
            for (StreamProcessingJob job : streams)
                job.run();
            latch.await();
        } catch (Exception e) {
            logger.error("Unknown Exceptions: {}", (Object) e.getStackTrace());
            logger.error("Closing");
            System.exit(-1);
        }

        System.exit(0);
    }

    /**
     * Configures and parses the command line options for the class
     *
     * @param args the String[] args from main
     * @throws ParseException exception if unable to parse options
     */
    private static void getCmdOptions(String[] args) throws ParseException {
        logger.info("Loading command line options: {}.{}", IoTMapperDataProcessing.class, "getOptions()");

        Options options = new Options();
        options.addOption("a", true, "non-default app id to use, must be unique in cluster");
        options.addOption("k", true, "bootstrap server string to use");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("a")) Configuration.KAFKA_ID_BASE = cmd.getOptionValue("a");
        if (cmd.hasOption("k")) Configuration.KAFKA_HOST = cmd.getOptionValue("k");

    }

    /**
     * Configures and parses the env options for the class
     *
     * @throws ParseException exception if unable to parse options
     */
    private static void getEnvVars() {
        String KAFKA_HOST_ENV_VAR = System.getenv().get(Configuration.KAFKA_HOST_ENV_VAR);
        String KAFKA_ID_BASE_ENV_VAR = System.getenv().get(Configuration.KAFKA_ID_BASE_ENV_VAR);
        String KAFKA_REQUEST_TIMEOUT_MS_CONFIG_ENV_VAR = System.getenv().get(Configuration.KAFKA_REQUEST_TIMEOUT_MS_CONFIG_ENV_VAR);
        String KAFKA_RETRY_BACKOFF_MS_CONFIG_ENV_VAR = System.getenv().get(Configuration.KAFKA_RETRY_BACKOFF_MS_CONFIG_ENV_VAR);
        String KAFKA_ID_LORAWAN_ENV_VAR = System.getenv().get(Configuration.KAFKA_ID_LORAWAN_ENV_VAR);
        String KAFKA_STATE_STORE_DIR_ENV_VAR = System.getenv().get(Configuration.KAFKA_STATE_STORE_DIR_ENV_VAR);
        String IOTMAPPER_DROP_OLD_RECORDS_ENV_VAR = System.getenv().get(Configuration.IOTMAPPER_DROP_OLD_RECORDS_ENV_VAR);

        if (KAFKA_HOST_ENV_VAR != null) Configuration.KAFKA_HOST = KAFKA_HOST_ENV_VAR;
        if (KAFKA_ID_BASE_ENV_VAR != null) Configuration.KAFKA_ID_BASE = KAFKA_ID_BASE_ENV_VAR;
        if (KAFKA_REQUEST_TIMEOUT_MS_CONFIG_ENV_VAR != null)
            Configuration.KAFKA_REQUEST_TIMEOUT_MS_CONFIG = KAFKA_REQUEST_TIMEOUT_MS_CONFIG_ENV_VAR;
        if (KAFKA_RETRY_BACKOFF_MS_CONFIG_ENV_VAR != null)
            Configuration.KAFKA_RETRY_BACKOFF_MS_CONFIG = KAFKA_RETRY_BACKOFF_MS_CONFIG_ENV_VAR;
        if (KAFKA_ID_LORAWAN_ENV_VAR != null) Configuration.KAFKA_ID_LORAWAN = KAFKA_ID_LORAWAN_ENV_VAR;
        if (KAFKA_STATE_STORE_DIR_ENV_VAR != null) Configuration.KAFKA_STATE_STORE_DIR = KAFKA_STATE_STORE_DIR_ENV_VAR;
        if (IOTMAPPER_DROP_OLD_RECORDS_ENV_VAR != null) Configuration.IOTMAPPER_DROP_OLD_RECORDS_VALUE = Boolean.parseBoolean(IOTMAPPER_DROP_OLD_RECORDS_ENV_VAR);
    }

}
