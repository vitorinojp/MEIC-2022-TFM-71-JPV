package org.iotmapper.server.info.data

import org.iotmapper.server.common.fiware.*
import org.iotmapper.server.info.model.LpwanInfoObject
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Repository
class InfoRepository(private val client: WebClient) {

    /**
     * Get a single info entity for a lpwan id
     * @param service the id of a given lpwan
     * @param id the id a single id in a lpwan
     * @return [Mono] with [LpwanInfoObject] with the single metric
     */
    fun getInfoSingleEntity(id: String, services: NgsiServices?): Mono<LpwanInfoObject>{
        return createWithHeaders(
            client,
            HttpMethod.GET,
            "$FIWARE_ENTITIES_PATH/$id?type=$IoT_MAPPER_LPWAN_INFO",
            services
        )
            .retrieve()
            .bodyToMono(LpwanInfoObject::class.java)
    }

    /**
     * Gets a limited list of metrics for a specific lpwan
     * @param limit the max number of returned items
     * @param offset starting point for returned items
     * @return [Mono] with an array of [LpwanInfoObject], from [offset] with up to [limit] size, for a given [service]
     */
    fun getList(limit: Int, offset: Int, services: NgsiServices?): Mono<Pair<Int?, Array<LpwanInfoObject>>>{
        var result: Mono<Pair<Int?, Array<LpwanInfoObject>>> = createWithHeaders(
            client,
            HttpMethod.GET,
            "$FIWARE_ENTITIES_PATH?type=$IoT_MAPPER_LPWAN_INFO",
            services,
            limit,
            offset
        ).exchangeToMono { response ->
            var totalCount: Int? = response
                .headers()
                .header("Fiware-Total-Count")
                .get(0)
                .toInt()

            return@exchangeToMono response
                .bodyToMono(Array<LpwanInfoObject>::class.java)
                .mapNotNull { array -> Pair(totalCount, array)  }
        }

        return result
    }
}
