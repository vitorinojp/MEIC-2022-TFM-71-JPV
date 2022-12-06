FROM quay.io/strimzi/kafka:0.28.0-kafka-3.1.0
USER root:root
COPY plugins/ /opt/kafka/plugins/

RUN chmod +rx /opt/kafka/plugins/ -R

USER 1001
