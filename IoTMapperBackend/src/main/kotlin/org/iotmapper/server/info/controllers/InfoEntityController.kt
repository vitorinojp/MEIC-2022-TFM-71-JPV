package org.iotmapper.server.info.controllers

import org.iotmapper.server.CacheSupplier
import org.iotmapper.server.common.DTO
import org.iotmapper.server.common.endpoints.INFO_ENTRY
import org.iotmapper.server.common.endpoints.INFO_LIST
import org.iotmapper.server.common.media.SIREN_MEDIA_TYPE_VALUE
import org.iotmapper.server.info.domain.InfoEntityService
import org.iotmapper.server.info.model.ListLpwansOutputModel
import org.iotmapper.server.info.model.SingleLpwanOutputModel
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * RESTfull API:
 * Controller for a list of info
 * @property domain the object to access domain logic
 */
@RestController
@RequestMapping(value= [INFO_ENTRY], produces = [SIREN_MEDIA_TYPE_VALUE])
class InfoEntityController(
        private val domain: InfoEntityService,
        private val cacheSupplier: CacheSupplier
) {
    private val miniCaching: CacheControl = cacheSupplier.getMiniCaching()

    /**
     * RESTfull API:
     * Get a list of info
     * @return a list of info
     */
    @GetMapping
    fun getInfoList(
        @PathVariable("lpwan") lpwan: String
    ): Mono<ResponseEntity<DTO<SingleLpwanOutputModel>>?> {
        return domain
            .get(lpwan)
            .mapNotNull { output ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(miniCaching)
                    .body(
                        SingleLpwanOutputModel(output).toDTO()
                    )
            }
    }
}