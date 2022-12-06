package org.iotmapper.server.info.domain

import org.iotmapper.server.common.fiware.NgsiServices
import org.iotmapper.server.info.data.InfoRepository
import org.iotmapper.server.info.model.LpwanInfoObject
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * RESTfull API:
 * Controller for a single info entry
 * @property repo the object to access the repository
 */
@Service
class InfoEntityService(
    private val repo: InfoRepository
) {

    /**
     * RESTfull API:
     * Get a single info entry
     * @param lpwan the id of the info entry
     * @return the info entry
     */
    fun get(lpwan: String): Mono<LpwanInfoObject> {
        return repo.getInfoSingleEntity(lpwan, NgsiServices.get())
    }

}