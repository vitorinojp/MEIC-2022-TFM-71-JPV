package org.iotmapper.server.common.fiware

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.StandardCharsets

/**
 * FIWARE Headers
 */
const val FIWARE_SERVICE_HEADER = "fiware-service"
const val FIWARE_SERVICE_PATH_HEADER = "fiware-servicepath"

/**
 * FIWARE Paths
 */
const val FIWARE_BASE_PATH = "/v2"
const val FIWARE_ENTITIES_PATH = "$FIWARE_BASE_PATH/entities"

/**
 * Holder for path and servicepath
 */
data class NgsiServices(val service: String, val subservice: String) {
    /**
     * Companion object for NgsiServices
     */
    companion object {
        /**
         * Create a new NgsiServices object, for the specified service, if none match, return for "lorawan"
         * @param service the service name to use
         */
        fun get(lpwan: String): NgsiServices {
            return when (lpwan) {
                "lorawan" -> forLorawan()
                else -> forLorawan()
            }
        }

        /**
         * Create a new NgsiServices object, for the general service
         * @return the NgsiServices object
         */
        fun get(): NgsiServices? {
            return null
        }

        /**
         * Convenience method for creating a new NgsiServices object for the lorawan service
         */
        fun forLorawan() = NgsiServices(LORAWAN_SERVICE, LORAWAN_SUBSERVICE)
    }
}

/**
 * Common creator for requests
 */
fun createWithHeaders(
    client: WebClient,
    method: HttpMethod,
    url: String,
    services: NgsiServices?,
    limits: Int = 0,
    offset: Int = 0,
    dateModified: Boolean = false,
    dateCreated: Boolean = false
): WebClient.RequestBodySpec {
    var finalURL = url

    if (limits != 0)
        finalURL = finalURL.plus("&limit=$limits")
    if (offset != 0)
        finalURL = finalURL.plus("&offset=$offset")

    finalURL = finalURL.plus("&options=count,keyValues")

    if (dateCreated)
        finalURL = finalURL.plus(",dateCreated")
    if (dateModified)
        finalURL = finalURL.plus(",dateModified")

    var clientRequest: WebClient.RequestBodySpec  = client
        .method(method)
        .uri(finalURL)
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)

    if (services != null) {
        clientRequest = clientRequest
            .header(FIWARE_SERVICE_HEADER, services.service)
            .header(FIWARE_SERVICE_PATH_HEADER, services.subservice)
    }

    return clientRequest
}