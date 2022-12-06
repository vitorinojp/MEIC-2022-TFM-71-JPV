package org.iotmapper.server.info.model

import org.iotmapper.server.common.DataModel

class LpwanInfoObject(
    val id: String,
    val type: String,
    val name: String,
    val dateLastUpdate: String?,
    val description: String,
    val metricsList: Array<MeasureInfoEntry>
): DataModel

class MeasureInfoEntry(
    val name: String,
    val description: String,
    val unit: String,
    val mandatory: Boolean
): DataModel