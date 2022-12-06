package org.iotmapper.server.metrics.controllers

import org.iotmapper.server.CacheSupplier
import org.iotmapper.server.common.DTO
import org.iotmapper.server.common.endpoints.METRICS_LIST
import org.iotmapper.server.common.geo.LocationFilterInputModel
import org.iotmapper.server.common.media.JSON_MEDIA_TYPE_VALUE
import org.iotmapper.server.common.media.SIREN_MEDIA_TYPE_VALUE
import org.iotmapper.server.metrics.domain.MetricsListService
import org.iotmapper.server.metrics.model.ListMetricsOutputModel
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * RESTfull API:
 * Controller for a list of metrics
 * @property domain the object to access domain logic
 */
@RestController
@RequestMapping(value = [METRICS_LIST], produces = [SIREN_MEDIA_TYPE_VALUE])
class MetricsListController(
    private val domain: MetricsListService,
    private val cacheSupplier: CacheSupplier
) {
    private val microCaching: CacheControl = cacheSupplier.getMicroCaching()

    /**
     * Implementation of the API endpoint to get a list of metrics
     * @param lpwan the id of the requested network
     * @param limit the max number of returned items, defaults to 50
     * @param offset starting point for returned items, defaults to 0
     */
    @GetMapping
    suspend fun getListSimple(
        @PathVariable("lpwan") lpwan: String,
        @RequestParam(name = "limit", required = false, defaultValue = "50") limit: Int,
        @RequestParam(name = "offset", required = false, defaultValue = "0") offset: Int,
        @RequestParam(name = "dateCreated", required = false, defaultValue = "false") dateCreated: Boolean,
        @RequestParam(name = "dateModified", required = false, defaultValue = "false") dateModified: Boolean
    ): Mono<ResponseEntity<DTO<ListMetricsOutputModel>>?> {
        return domain
            .getList(lpwan, limit, offset, dateModified = dateModified, dateCreated = dateCreated)
            .mapNotNull { output ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(microCaching)
                    .body(
                        ListMetricsOutputModel(output.first, output.second).toDTO()
                    )
            }
    }

    /**
     * Implementation of the API endpoint to search metrics in a given area
     * @param lpwan the id of the requested network
     * @param limit the max number of returned items, defaults to 50
     * @param offset starting point for returned items, defaults to 0
     * @param body search filters, see [LocationFilterInputModel]
     */
    @PostMapping(consumes = [JSON_MEDIA_TYPE_VALUE])
    fun getListOfRegion(
        @PathVariable("lpwan") lpwan: String,
        @RequestParam(name = "limit", required = false, defaultValue = "50") limit: Int,
        @RequestParam(name = "offset", required = false, defaultValue = "0") offset: Int,
        @RequestParam(name = "dateCreated", required = false) dateCreated: Boolean = false,
        @RequestParam(name = "dateModified", required = false) dateModified: Boolean = false,
        @RequestBody body: LocationFilterInputModel
    ): Mono<DTO<ListMetricsOutputModel>?> {
        return domain
            .getList(lpwan, body, limit, offset, dateModified = dateModified, dateCreated = dateCreated)
            .mapNotNull { output -> ListMetricsOutputModel(output.first, output.second).toDTO() }
    }
}