package org.iotmapper.server.metrics.model

import org.iotmapper.server.common.DTO
import org.iotmapper.server.common.DTOCreator
import org.iotmapper.server.common.media.SirenEntity

class ListMetricsOutputModel(
    val totalCount: Int?,
    val list: Array<LpwanCalculatedMetricsObject>?
) : DTOCreator<ListMetricsOutputModel>, DTO<ListMetricsOutputModel> {

    override fun toDTO(): DTO<ListMetricsOutputModel> {
        return toSirenObject()
    }

    fun toSirenObject(): SirenEntity<ListMetricsOutputModel> {
        return SirenEntity(
            clazz = listOf("metrics", "collection"),
            properties = this,
        )
    }
}