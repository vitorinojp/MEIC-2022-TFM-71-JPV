package org.iotmapper.server.common.endpoints

import org.iotmapper.server.common.media.NotFoundException
import org.iotmapper.server.common.media.PROBLEM_JSON_MEDIA_TYPE_VALUE
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FallbackController {
    class NoHandlerFoundException : NotFoundException(
        title = "No Handler Found",
        detail = "Please check the documentation to make sure you're using the right address."
    )

    @RequestMapping(path = ["**"], produces = [PROBLEM_JSON_MEDIA_TYPE_VALUE])
    fun noHandlerFound(): String {
        throw NoHandlerFoundException()
    }
}