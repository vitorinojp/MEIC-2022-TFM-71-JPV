package org.iotmapper.server.gateways.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

class GatewayEntityController {

    @GetMapping
    fun getEntity(@PathVariable("lpwan") lpwan: String, @PathVariable("id") id: String) {
        TODO("Not yet implemented")
    }
}