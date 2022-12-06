package org.iotmapper.server.info.controllers

import org.iotmapper.server.CacheSupplier
import org.iotmapper.server.common.DTO
import org.iotmapper.server.common.endpoints.INFO_LIST
import org.iotmapper.server.common.media.SIREN_MEDIA_TYPE_VALUE
import org.iotmapper.server.info.domain.InfoListService
import org.iotmapper.server.info.model.ListLpwansOutputModel
import org.iotmapper.server.metrics.model.ListMetricsOutputModel
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * RESTfull API:
 * Controller for a list of info
 * @property domain the object to access domain logic
 */
@RestController
@RequestMapping(value= [INFO_LIST], produces = [SIREN_MEDIA_TYPE_VALUE])
class InfoListController(
    private val domain: InfoListService,
    private val cacheSupplier: CacheSupplier
){
    private val miniCaching: CacheControl = cacheSupplier.getMiniCaching()

    /**
     * Implementation of the API endpoint to get a list of info about lpwan
     * @param lpwanQuery the lpwan id to filter by
     * @param limit the max number of returned items, defaults to 50
     * @param offset starting point for returned items, defaults to 0
     * @return a list of info about lpwan
     */
    @GetMapping
    suspend fun getListSimple(
        @RequestParam(name = "lpwanquery", required = false) lpwanQuery: String?,
        @RequestParam(name = "limit", required = false, defaultValue = "50") limit: Int,
        @RequestParam(name = "offset", required = false, defaultValue = "0") offset: Int
    ): Mono<ResponseEntity<DTO<ListLpwansOutputModel>>?> {
        return domain
            .getList(lpwanQuery, limit, offset)
            .mapNotNull { output ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(miniCaching)
                    .body(
                        ListLpwansOutputModel(output.first, output.second).toDTO()
                    )
            }
    }

}