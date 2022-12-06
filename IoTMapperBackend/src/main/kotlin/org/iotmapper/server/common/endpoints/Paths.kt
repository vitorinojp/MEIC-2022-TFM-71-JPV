package org.iotmapper.server.common.endpoints

/**
 * Unified list of default paths for rescources
 */

/**
 * Default hostname
 */
const val HOSTNAME = "http://localhost:8080"

/**
 * Version
 */

const val API_VERSION = "v1"

/**
 * Default Web API base path
 */
const val ROOT_PATH = "/api/$API_VERSION"

/**
 * Base paths for endpoints by type
 */
const val METRICS_BASE_PATH = "$ROOT_PATH/metrics"
const val GATEWAYS_BASE_PATH = "$ROOT_PATH/gateways"
const val INFO_BASE_PATH = "$ROOT_PATH/info"

/**
 * Metrics paths
 */
const val METRICS_LIST = "$METRICS_BASE_PATH/list/{lpwan}"
const val METRICS_ENTRY = "$METRICS_BASE_PATH/entry/{lpwan}/{id}"

/**
 * Gateways paths
 */
const val GATEWAYS_LIST = "$GATEWAYS_BASE_PATH/list/{lpwan}"
const val GATEWAYS_ENTRY = "$GATEWAYS_BASE_PATH/entry/{lpwan}/{id}"

/**
 * Info paths
 */
const val INFO_LIST = "$INFO_BASE_PATH/list"
const val INFO_ENTRY = "$INFO_BASE_PATH/entry/{lpwan}"