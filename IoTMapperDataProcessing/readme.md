# IoTMapperDataProcessing

All the code for the creation of the data processing for IoTMapper.

## Implemenattion

The data processing is set up as a topology in a [Kafka Streams](https://kafka.apache.org/documentation/streams/)
application, and therefore depends on a [Apache Kafka](https://kafka.apache.org/) cluster. For 1.0.0 there must be a
topic in the format "(.\*)xfffffx002f4(.\*)".

## Organization

Inside the *src.main.java.org.iotmapper* the modules are organized as follows.

* **IoTMapperDataProcessing:** main class that creates and runs the topologies;
* **models:** data models;
    * **cygnus:** to read from Cygnus;
    * **kafka:** data transfer inside kafka;
* **streams:** modules that help define actions specific to Kafka Streams;
    * **aggregation:** aggregations helpers;
    * **mapping:** mapping helpers;
    * **lpwan**
* **lpwan:** abstractions for domains details specific to LPWAN protocols;
* **commons:** functionality common across multiple modules.
    * **utils:** helpers;
    * **configs** configuration definitions.