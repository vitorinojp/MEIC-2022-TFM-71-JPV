package org.iotmapper.server.info.domain

import org.iotmapper.server.common.fiware.NgsiServices
import org.iotmapper.server.info.data.InfoRepository
import org.iotmapper.server.info.model.LpwanInfoObject
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * Domain object for list of info
 * @property repo repository to use for info
 */
@Service
class InfoListService(
    private val repo: InfoRepository
) {

    /**
     * Get info list
     * @param lpwanQuery query to use for filtering
     * @param limit limit of info objects to return
     * @param offset offset to use for paging
     * @return info list
     */
    fun getList(lpwanQuery: String?, limit: Int, offset: Int): Mono<Pair<Int?, Array<LpwanInfoObject>>> {
        return if (lpwanQuery == null) {
            repo.getList(limit, offset, NgsiServices.get())
        } else {
            repo
                .getList(limit, offset, NgsiServices.get())
                .mapNotNull { it ->
                    val list = it.second.filter {
                        it.name.contains(lpwanQuery)
                    }.toTypedArray()
                    Pair(it.first, list)
                }
        }
    }
}