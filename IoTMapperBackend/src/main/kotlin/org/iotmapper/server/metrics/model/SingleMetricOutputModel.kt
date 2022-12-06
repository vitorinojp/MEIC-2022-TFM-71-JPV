package org.iotmapper.server.metrics.model

import org.iotmapper.server.common.DTO
import org.iotmapper.server.common.DTOCreator
import org.iotmapper.server.common.media.SirenEntity

class SingleMetricOutputModel(
    val result: LpwanCalculatedMetricsObject?
): DTOCreator<SingleMetricOutputModel>, DTO<SingleMetricOutputModel> {

    override fun toDTO(): DTO<SingleMetricOutputModel> {
        return toSirenObject()
    }

    fun toSirenObject(): SirenEntity<SingleMetricOutputModel> {
        return SirenEntity(
            clazz = listOf("metrics", "collection"),
            properties = this,
        )
    }
}