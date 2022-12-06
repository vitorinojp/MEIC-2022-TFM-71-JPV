package org.iotmapper.server.common

/**
 * Describes a generic domain object that may be converted in a DTO
 */
interface DTOCreator<T> {

    /**
     * Convert the object
     */
    fun toDTO(): DTO<T>
}

interface DTO<T>