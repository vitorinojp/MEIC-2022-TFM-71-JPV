package org.iotmapper.server.metrics.controllers

import org.iotmapper.server.common.DTO
import org.iotmapper.server.common.endpoints.METRICS_ENTRY
import org.iotmapper.server.common.media.SIREN_MEDIA_TYPE_VALUE
import org.iotmapper.server.metrics.domain.MetricsEntityService
import org.iotmapper.server.metrics.model.SingleMetricOutputModel
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * RESTfull API:
 * Controller for a single metric
 * @property domain object to access the domain logic
 */
@RestController
@RequestMapping(value = [METRICS_ENTRY], produces = [SIREN_MEDIA_TYPE_VALUE])
class MetricsEntityController(private val domain: MetricsEntityService) {

    /**
     * Implementation of the API endpoint to Get the value of a single metric
     * @param lpwan the id of the requested network
     * @param id the id of the given metric
     */
    @GetMapping
    fun getEntity(
        @PathVariable("lpwan") lpwan: String,
        @PathVariable("id") id: String,
        @RequestParam(name = "dateCreated", required = false) dateCreated: Boolean = false,
        @RequestParam(name = "dateModified", required = false) dateModified: Boolean = false
    ): Mono<DTO<SingleMetricOutputModel>?> {
        return domain
            .getEntity(lpwan, id, dateCreated, dateModified)
            .mapNotNull { output -> SingleMetricOutputModel(output).toDTO() }
    }
}