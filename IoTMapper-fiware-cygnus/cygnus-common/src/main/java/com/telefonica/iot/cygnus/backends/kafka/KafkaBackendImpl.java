/**
 * Copyright 2015-2017 Telefonica Investigación y Desarrollo, S.A.U
 *
 * This file is part of fiware-cygnus (FIWARE project).
 *
 * fiware-cygnus is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * fiware-cygnus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with fiware-cygnus. If not, see
 * http://www.gnu.org/licenses/.
 *
 * For those usages not covered by the GNU Affero General Public License please contact with iot_support at tid dot es
 */
package com.telefonica.iot.cygnus.backends.kafka;

import com.telefonica.iot.cygnus.log.CygnusLogger;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 *
 * @author pcoello25
 */

public class KafkaBackendImpl implements KafkaBackend {
    
    private KafkaProducer<String, String> kafkaProducer;
    private static final CygnusLogger LOGGER = new CygnusLogger(KafkaBackendImpl.class);
    private AdminClient adminClient;
    
    /**
     * Constructor.
     * @param brokerList
     * @param batchSize
     */
    public KafkaBackendImpl(String brokerList, int batchSize) {
        LOGGER.debug("Creating persistence backend.");
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        adminClient = KafkaAdminClient.create(properties);
        kafkaProducer = new KafkaProducer<String, String>(properties);
    } // KafkaBackendImpl

    @Override
    public boolean topicExists(String topic) throws Exception {
        LOGGER.debug("Checking if topic '" + topic + "' already exists.");
        return adminClient.listTopics().names().get().stream().anyMatch(topic::equals);
    } // topicExists

    @Override
    public void createTopic(String topic, int partitions, short replicationFactor) throws ExecutionException, InterruptedException {
        CreateTopicsResult result = adminClient
                .createTopics(Collections.singleton(new NewTopic(topic, partitions, replicationFactor)));
        result.values().get(topic).get();
        LOGGER.debug("Creating topic: " + topic + " , partitions: " + partitions
                + " , " + "replication factor: " + replicationFactor + ".");
    } // createTopic

    @Override
    public void send(ProducerRecord<String, String> record) {
        kafkaProducer.send(record);
        LOGGER.debug("Record: '" + record + "' sent to Kafka.");
    } // send
    
    /**
     * Sets the Kafka producer.
     * @param producer
     */
    protected void setKafkaProducer(KafkaProducer producer) {
        LOGGER.debug("Setting the producer to the backend.");
        this.kafkaProducer = producer;
    } // setKafkaProducer
    
} // KafkaBackendImpl

