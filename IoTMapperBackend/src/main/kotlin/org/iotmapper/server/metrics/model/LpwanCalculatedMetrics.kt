package org.iotmapper.server.metrics.model

import com.fasterxml.jackson.annotation.JsonAlias
import org.geojson.Feature
import org.iotmapper.server.common.DataModel

class LpwanCalculatedMetricsObject(
    val id: String,
    val type: String,
    val dateLastGwUpdate: String,
    val dateLastUpdate: String,
    val dateCreated: String?,
    val dateModified: String?,
    @JsonAlias("GeoHash") val mapTile: String,
    val gwId: String?,
    val location: Feature,
    val measures: Array<LpwanCalculatedMeasureEntry>
) : DataModel

class LpwanCalculatedMeasureEntry(
    val name: String,
    val value: Double,
    val count: Long,
    val type: String?
) : DataModel
