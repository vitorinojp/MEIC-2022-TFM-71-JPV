package org.iotmapper.server.info.model

import org.iotmapper.server.common.DTO
import org.iotmapper.server.common.DTOCreator
import org.iotmapper.server.common.media.SirenEntity

class SingleLpwanOutputModel(
    var result: LpwanInfoObject?
): DTOCreator<SingleLpwanOutputModel>, DTO<SingleLpwanOutputModel> {

    override fun toDTO(): DTO<SingleLpwanOutputModel> {
        return this.toSirenObject()
    }

    fun toSirenObject(): SirenEntity<SingleLpwanOutputModel> {
        return SirenEntity(
            clazz = listOf("info", "single"),
            properties = this
        )
    }
}