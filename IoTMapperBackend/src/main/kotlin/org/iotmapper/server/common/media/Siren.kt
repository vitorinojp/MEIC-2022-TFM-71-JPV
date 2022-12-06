package org.iotmapper.server.common.media

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.iotmapper.server.common.DTO
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

/**
 * Class whose instances represent entities as they are represented in Siren.
 */
data class SirenEntity<T>(
    val title: String? = null,
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: T? = null,
    val entities: List<SirenSubEntity>? = null,
    val actions: List<SirenAction>? = null,
    val links: List<SirenLink>? = null
) : DTO<T>

fun <T> SirenEntity<T>.toEmbeddedRepresentation(rel: List<String>) = SirenSubEntity.EmbeddedRepresentation<T>(
    this.title,
    this.clazz,
    rel,
    this.properties,
    this.entities,
    this.actions,
    this.links
)

/**
 * Classes whose instances represent sub-entities as they are represented in Siren.
 */
sealed class SirenSubEntity {
    data class EmbeddedRepresentation<T>(
        val title: String? = null,
        @JsonProperty("class") val clazz: List<String>? = null,
        val rel: List<String>,
        val properties: T? = null,
        val entities: List<SirenSubEntity>? = null,
        val actions: List<SirenAction>? = null,
        val links: List<SirenLink>? = null
    ) : SirenSubEntity()

    data class EmbeddedLink(
        val title: String? = null,
        @JsonProperty("class") val clazz: List<String>? = null,
        val rel: List<String>,
        val href: URI,
        @JsonSerialize(using = ToStringSerializer::class) val type: MediaType? = null
    ) : SirenSubEntity()
}

/**
 * Class whose instances represent links that are included in a siren entity (and sub-entity).
 */
data class SirenLink
    (
    val title: String? = null,
    @JsonProperty("class") val clazz: List<String>? = null,
    val rel: List<String>,
    val href: URI,
    @JsonSerialize(using = ToStringSerializer::class) val type: MediaType? = null
)

fun SirenLink.toEmbeddedLink() = SirenSubEntity.EmbeddedLink(
    this.title,
    this.clazz,
    this.rel,
    this.href,
    this.type
)

/**
 * Class whose instances represent actions that are included in a siren entity (and sub-entity).
 */
data class SirenAction
    (
    val name: String,
    val title: String? = null,
    @JsonProperty("class") val clazz: List<String>? = null,
    val method: HttpMethod? = HttpMethod.GET,
    val href: URI,
    @JsonSerialize(using = ToStringSerializer::class) val type: MediaType? = MediaType.APPLICATION_FORM_URLENCODED,
    val fields: List<Field>? = null
) {
    /**
     * Class whose instances represent fields that are included in a siren action.
     */
    data class Field
        (
        val title: String? = null,
        val name: String,
        @JsonProperty("class") val clazz: List<String>? = null,
        val type: String? = "text",
        val value: String? = null
    )
}