package org.iotmapper.server.metrics.domain

import org.iotmapper.server.common.fiware.NgsiServices
import org.iotmapper.server.metrics.data.MetricsRepository
import org.iotmapper.server.metrics.model.LpwanCalculatedMetricsObject
import org.iotmapper.server.metrics.model.SingleMetricOutputModel
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * Domain logic for single metric objects
 * @property repo repository for access to metrics
 */
@Service
class MetricsEntityService(private val repo: MetricsRepository) {

    /**
     * Get a single metric for a lpwan and id pair
     * @param lpwan the id of a given lpwan
     * @param id the id a single id in a lpwan
     * @return [Mono] with [SingleMetricOutputModel] with the single metric
     */
    fun getEntity(lpwan: String, id: String, dateCreated: Boolean = false, dateModified: Boolean = false): Mono<LpwanCalculatedMetricsObject> {

        return repo
            .getMetricsSingleEntity(id, NgsiServices.get(lpwan), dateCreated, dateModified)
    }
}