# Uses the OpenJdk built by Adoptium (https://adoptium.net/), previously AdoptOpenJdk, know as Eclipse Temurin (RocksDB requires glibc!)
ARG BUILD_IMAGE=eclipse-temurin:11

## Base image to reuse, keeping vars but not files
FROM $BUILD_IMAGE AS base

# ENV vars as ARGS=<default_value>, may be replaced by passing --build-args to docker/podman/buildah
ARG IOTMAPPER_BUILD_PATH=/opt/iotmapper/iotmapperdataprocessing
ARG IOTMAPPER_BUILD_JAR_VERSION=1.0
ARG IOTMAPPER_BUILD_JAR_NAME=IoTMapperDataProcessing
# Set needed ARGs as ENV vars (for entrypoint)
ENV IOTMAPPER_JAR="$IOTMAPPER_BUILD_JAR_NAME-$IOTMAPPER_BUILD_JAR_VERSION.jar"

## Build stage
FROM base AS builder

# Path in container to use
WORKDIR $IOTMAPPER_BUILD_PATH/

# Set up maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Dependencies with wrapper
RUN ./mvnw dependency:go-offline

# Copy only the minimal required to build
COPY src src/

# Build
RUN ./mvnw package


## Runnable stage (cleanned)
FROM base AS runnable

# Path in container to use
WORKDIR $IOTMAPPER_BUILD_PATH/

# Copy what is needed to run
COPY --from=builder "target/$IOTMAPPER_JAR" ./
COPY docker/entrypoint.sh ./

ENTRYPOINT ["sh", "./entrypoint.sh"]
