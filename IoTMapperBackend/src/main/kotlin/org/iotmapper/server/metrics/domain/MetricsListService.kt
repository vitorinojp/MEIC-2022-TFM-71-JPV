package org.iotmapper.server.metrics.domain

import org.iotmapper.server.common.fiware.NgsiServices
import org.iotmapper.server.common.geo.LocationFilterInputModel
import org.iotmapper.server.metrics.data.MetricsRepository
import org.iotmapper.server.metrics.model.ListMetricsOutputModel
import org.iotmapper.server.metrics.model.LpwanCalculatedMetricsObject
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * Domain object for list of metrics
 * @property repo repository to use for metrics
 */
@Service
class MetricsListService(private val repo: MetricsRepository) {

    /**
     * Gets a limited list of metrics for a specific lpwan
     * @param lpwan the id of the requested network
     * @param limit the max number of returned items
     * @param offset starting point for returned items
     * @return [ListMetricsOutputModel] list of metrics, from [offset] with up to [limit] size, for a given [lpwan]
     */
    fun getList(
        lpwan: String,
        limit: Int,
        offset: Int,
        dateCreated: Boolean = false,
        dateModified: Boolean = false
    ): Mono<Pair<Int?, Array<LpwanCalculatedMetricsObject>>> {
        return repo
            .getList(limit, offset, NgsiServices.get(lpwan), dateCreated, dateModified)
    }

    /**
     * Searches a limited list of metrics for a specific lpwan in a given location. Calls [LocationFilterInputModel.isValid] to check if the body is valid.
     * @param lpwan the id of the requested network
     * @param body the [LocationFilterInputModel] with the search parameters
     * @param limit the max number of returned items
     * @param offset starting point for returned items
     * @return [ListMetricsOutputModel] list of metrics, from [offset] with up to [limit] size, for a given [lpwan] and [body]
     */
    fun getList(
        lpwan: String,
        body: LocationFilterInputModel,
        limit: Int,
        offset: Int,
        dateCreated: Boolean = false,
        dateModified: Boolean = false
    ): Mono<Pair<Int?, Array<LpwanCalculatedMetricsObject>>>{
        val validBody = body.isValid()
        return repo
            .getListithGeoFilter(validBody.first, validBody.second, limit, offset, NgsiServices.get(lpwan), dateCreated, dateModified)
    }
}