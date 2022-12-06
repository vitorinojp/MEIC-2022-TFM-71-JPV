package org.iotmapper.server.common.media

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Interface used on exceptions that can be convertible to problem json
 */

interface ConvertibleToProblemJson {
    fun toProblemJson(): ProblemJson
}

/**
 * Class used for errors, based on the [Problem Json documentation] (https://tools.ietf.org/html/rfc7807)
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemJson
    (
    val type: String? = null,
    val title: String? = null,
    val detail: String? = null,
    val instance: String? = null,
    val status: Int? = null
)

open class ProblemJsonException
    (
    val type: String? = null,
    val title: String? = null,
    val detail: String? = null,
    val instance: String? = null,
    val status: Int? = null
) : ConvertibleToProblemJson, RuntimeException() {
    override fun toProblemJson(): ProblemJson {
        return ProblemJson(
            this.type,
            this.title,
            this.detail,
            this.instance,
            this.status
        )
    }
}

open class BadRequestException(
    type: String? = null,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : ProblemJsonException(type, title, detail, instance, 400)

class InvalidPageParameterException : BadRequestException(
    title = "Invalid Page Parameter",
    detail = "The request parameter page must be a number greater than 0."
)

class MissingBodyParametersException(parameters: List<String>) : BadRequestException(
    title = "Missing Body " + if (parameters.size == 1) "Parameter" else "Parameters",
    detail = if (parameters.size == 1) parameters[0] else parameters.reduceRight { p1: String, p2: String -> "$p1; $p2" }
)

class InvalidParametersException(parameters: List<String>) : BadRequestException(
    title = "Invalid " + if (parameters.size == 1) "Parameter" else "Parameters",
    detail = if (parameters.size == 1) parameters[0] else parameters.reduceRight { p1: String, p2: String -> "$p1; $p2" }
)

class BlankBodyParametersException(parameters: List<String>) : BadRequestException(
    title = "Blank Body " + if (parameters.size == 1) "Parameter" else "Parameters",
    detail = if (parameters.size == 1) parameters[0] else parameters.reduceRight { p1: String, p2: String -> "$p1; $p2" }
)

open class UnauthorizedException(
    type: String? = null,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : ProblemJsonException(type, title, detail, instance, 401)

open class ForbiddenException(
    type: String? = null,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : ProblemJsonException(type, title, detail, instance, 403)

open class NotFoundException(
    type: String? = null,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : ProblemJsonException(type, title, detail, instance, 404)

open class ConflictException(
    type: String? = null,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : ProblemJsonException(type, title, detail, instance, 409)

open class UnsupportedMediaTypeException(
    type: String? = null,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : ProblemJsonException(type, title, detail, instance, 415)