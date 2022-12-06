package org.iotmapper.server.metrics.controllers

import org.iotmapper.server.ApplicationConfig
import org.iotmapper.server.CacheSupplier
import org.iotmapper.server.HttpClientsForRepositories
import org.iotmapper.server.common.endpoints.METRICS_LIST
import org.iotmapper.server.metrics.data.MetricsRepository
import org.iotmapper.server.metrics.domain.MetricsListService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext

@ExtendWith(SpringExtension::class)
@WebAppConfiguration("file:application.properties")
@ContextConfiguration(
    classes = arrayOf(
        ApplicationConfig::class,
        HttpClientsForRepositories::class,
        CacheSupplier::class,
        MetricsRepository::class,
        MetricsListService::class,
        MetricsListController::class
    )
)
internal class MetricsListControllerTest {
    @Autowired
    lateinit var wac: WebApplicationContext
    lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() {
        client = MockMvcWebTestClient.bindToApplicationContext(wac).build()
    }

    @Autowired
    lateinit var env: Environment

    @Test
    fun getListSimple() {
        var result = client
            .get().uri(METRICS_LIST, "lorawan")
            .exchange()
            .expectBody()
            .returnResult()
        var body = result.responseBody.toString()
    }
}