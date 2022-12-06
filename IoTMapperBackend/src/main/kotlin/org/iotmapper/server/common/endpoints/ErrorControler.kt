package org.iotmapper.server.common.endpoints


import org.iotmapper.server.CacheSupplier
import org.iotmapper.server.common.media.*
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class ErrorController(private val cacheSupplier: CacheSupplier) {
    private val errorCaching: CacheControl = cacheSupplier.getErrorCaching()

    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorized(ex: UnauthorizedException): ResponseEntity<ProblemJson> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .header("WWW-Authenticate", "Basic realm=\"Personal Data\", charset=\"UTF-8\"")
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(ex.toProblemJson())
    }

    @ExceptionHandler(ForbiddenException::class)
    fun forbidden(ex: ForbiddenException): ResponseEntity<ProblemJson> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(ex.toProblemJson())
    }

    @ExceptionHandler(BadRequestException::class)
    fun badRequest(ex: BadRequestException): ResponseEntity<ProblemJson> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(ex.toProblemJson())
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ProblemJson> {
        val body = ProblemJson(
            status = 400,
            title = "Http Body Deserialization Failed",
            detail = ex.mostSpecificCause.localizedMessage
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(body)
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun missingRequestHeader(ex: MissingRequestHeaderException): ResponseEntity<ProblemJson> {
        val body = ProblemJson(
            status = 400,
            title = "Missing Request Header",
            detail = "The header ${ex.headerName} is missing."
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(body)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun missingRequestParameter(ex: MissingServletRequestParameterException): ResponseEntity<ProblemJson> {
        val body = ProblemJson(
            status = 400,
            title = "Missing Request Parameter",
            detail = "The required ${ex.parameterType} parameter \'${ex.parameterName}\' is not present."
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(body)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun methodArgumentTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<ProblemJson> {
        val body = ProblemJson(
            status = 400,
            title = "Method Argument Type Mismatch",
            detail = ex.name + " should be of type " + ex.requiredType?.name + "."
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(body)
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFound(ex: NotFoundException): ResponseEntity<ProblemJson> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(ex.toProblemJson())
    }

    @ExceptionHandler(UnsupportedMediaTypeException::class)
    fun unsupportedMediaType(ex: UnsupportedMediaTypeException): ResponseEntity<ProblemJson> {
        return ResponseEntity
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(ex.toProblemJson())
    }

    @ExceptionHandler(ConflictException::class)
    fun conflict(
        req: HttpServletRequest,
        resp: HttpServletResponse,
        ex: ConflictException
    ): ResponseEntity<ProblemJson> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(ex.toProblemJson())
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun internalServerError(ex: Exception): ResponseEntity<ProblemJson> {
        val body = ProblemJson(
            status = 500,
            title = ex.javaClass.name,
            detail = ex.message
        )

        return ResponseEntity
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .cacheControl(errorCaching)
            .body(body)
    }
}
