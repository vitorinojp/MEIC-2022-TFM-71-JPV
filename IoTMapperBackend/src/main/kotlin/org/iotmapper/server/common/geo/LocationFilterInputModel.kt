package org.iotmapper.server.common.geo

import org.iotmapper.server.common.media.BlankBodyParametersException
import org.iotmapper.server.common.media.InvalidParametersException
import org.iotmapper.server.common.media.MissingBodyParametersException

/**
 * Input model for a filter by geographic area
 * @param maxDistance maximum distance form point
 * @param point centering for the search, a custom format as [GeoTools.GeoPair]
 */
class LocationFilterInputModel(val maxDistance: Int?, val point: GeoTools.GeoPair?) {

    /**
     * Checks if the body is valid and extracts the null checked representation
     * @return [Pair] with <[Int], [GeoTools.GeoPair]>
     * @throws [BlankBodyParametersException] empty body
     * @throws [MissingBodyParametersException] one or more parameters is missing
     * @throws [InvalidParametersException] one or more parameters is malformed
     */
    fun isValid(): Pair<Int, GeoTools.GeoPair> {
        val missingParams = mutableListOf<String>()
        val blankParams = mutableListOf<String>()
        val invalidParams = mutableListOf<String>()

        // The values should *not* be used, present only to help null checking
        var nullCheckedMaxDistance: Int = 0
        var nullCheckedPoint: GeoTools.GeoPair = GeoTools.GeoPair(0.0, 0.0)

        if (maxDistance == null)
            missingParams.add("maxDistance")
        else if (maxDistance <= 0)
            invalidParams.add("maxDistance: must be more than 0")
        else
            nullCheckedMaxDistance = maxDistance

        if (point == null)
            missingParams.add("point")
        else if (point.isEmpty)
            blankParams.add("point")
        else
            nullCheckedPoint = point

        if (missingParams.size > 1)
            throw MissingBodyParametersException(missingParams)
        else if (invalidParams.size > 1)
            throw InvalidParametersException(invalidParams)
        else if (blankParams.isNotEmpty())
            throw BlankBodyParametersException(blankParams)
        else
            return Pair(nullCheckedMaxDistance, nullCheckedPoint)
    }
}
