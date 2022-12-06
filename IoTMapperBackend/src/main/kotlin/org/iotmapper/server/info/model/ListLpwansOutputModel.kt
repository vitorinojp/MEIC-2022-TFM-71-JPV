package org.iotmapper.server.info.model

import org.iotmapper.server.common.DTO
import org.iotmapper.server.common.DTOCreator
import org.iotmapper.server.common.media.SirenEntity


class ListLpwansOutputModel(
    val totalCount: Int?,
    val list: Array<LpwanInfoObject>?
): DTOCreator<ListLpwansOutputModel>, DTO<ListLpwansOutputModel> {

    override fun toDTO(): DTO<ListLpwansOutputModel> {
        return this.toSirenObject()
    }

    fun toSirenObject(): SirenEntity<ListLpwansOutputModel> {
        return SirenEntity(
            clazz = listOf("info", "collection"),
            properties = this
        )
    }
}