package org.iotmapper.server.metrics.data

import org.iotmapper.server.common.fiware.*
import org.iotmapper.server.common.geo.GeoTools
import org.iotmapper.server.common.geo.LocationFilterInputModel
import org.iotmapper.server.metrics.model.LpwanCalculatedMetricsObject
import org.iotmapper.server.metrics.model.SingleMetricOutputModel
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 * Central repository for metrics
 */
@Repository
class MetricsRepository(private val client: WebClient) {

    /**
     * Get a single metric for a lpwan and id pair
     * @param service the id of a given lpwan
     * @param id the id a single id in a lpwan
     * @return [Mono] with [SingleMetricOutputModel] with the single metric
     */
    fun getMetricsSingleEntity(
        id: String,
        services: NgsiServices,
        dateCreated: Boolean = false,
        dateModified: Boolean = false
    ): Mono<LpwanCalculatedMetricsObject> {
        return createWithHeaders(
            client,
            HttpMethod.GET,
            "$FIWARE_ENTITIES_PATH/$id?type=$IoT_MAPPER_METRICS_RESULTS",
            services,
            dateCreated = dateCreated,
            dateModified = dateModified
        )
            .retrieve()
            .bodyToMono(LpwanCalculatedMetricsObject::class.java)
    }

    /**
     * Gets a limited list of metrics for a specific lpwan
     * @param service the id of the requested network
     * @param limit the max number of returned items
     * @param offset starting point for returned items
     * @return [Mono] with an array of [LpwanCalculatedMetricsObject], from [offset] with up to [limit] size, for a given [service]
     */
    fun getList(
        limit: Int, offset: Int,
        services: NgsiServices,
        dateCreated: Boolean = false,
        dateModified: Boolean = false
    ): Mono<Pair<Int?, Array<LpwanCalculatedMetricsObject>>> {
       var result: Mono<Pair<Int?, Array<LpwanCalculatedMetricsObject>>> = createWithHeaders(
            client,
            HttpMethod.GET,
            "$FIWARE_ENTITIES_PATH?type=$IoT_MAPPER_METRICS_RESULTS",
            services,
            limit,
            offset,
            dateCreated,
            dateModified
        ).exchangeToMono { response ->
           var totalCount: Int? = response
               .headers()
               .header("Fiware-Total-Count")
               .get(0)
               .toInt()

           return@exchangeToMono response
               .bodyToMono(Array<LpwanCalculatedMetricsObject>::class.java)
               .mapNotNull { array -> Pair(totalCount, array)  }
        }

        return result
    }

    /**
     * Searches a limited list of metrics for a specific lpwan in a given location. Calls [LocationFilterInputModel.isValid] to check if the body is valid.
     * @param service the id of the requested network
     * @param body the [LocationFilterInputModel] with the search parameters
     * @param limit the max number of returned items
     * @param offset starting point for returned items
     * @return [Mono] with an array of [LpwanCalculatedMetricsObject], from [offset] with up to [limit] size, for a given [service] and [body]
     */
    fun getListithGeoFilter(
        maxDistance: Int,
        point: GeoTools.GeoPair,
        limit: Int,
        offset: Int,
        services: NgsiServices,
        dateCreated: Boolean = false,
        dateModified: Boolean = false
    ): Mono<Pair<Int?, Array<LpwanCalculatedMetricsObject>>> {
        TODO("Fix filtering")
        return createWithHeaders(
            client,
            HttpMethod.GET,
            "$FIWARE_ENTITIES_PATH?type=$IoT_MAPPER_METRICS_RESULTS&options=keyValues",
            services,
            limit,
            offset,
            dateCreated,
            dateModified
        ).exchangeToMono { response ->
            var totalCount: Int? = response
                .headers()
                .header("Fiware-Total-Count")
                .get(0)
                .toInt()

            return@exchangeToMono response
                .bodyToMono(Array<LpwanCalculatedMetricsObject>::class.java)
                .mapNotNull { array -> Pair(totalCount, array)  }
        }
    }

}