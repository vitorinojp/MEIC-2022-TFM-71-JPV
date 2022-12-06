package org.iotmapper.server

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import org.iotmapper.server.common.media.JSON_HOME_MEDIA_TYPE
import org.iotmapper.server.common.media.JSON_MEDIA_TYPE
import org.iotmapper.server.common.media.PROBLEM_JSON_MEDIA_TYPE
import org.iotmapper.server.common.media.SIREN_MEDIA_TYPE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.http.CacheControl
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * App to be created by Spring. "Actual" main
 */
@SpringBootApplication
class IoTMapperBackendApplication

/**
 * Configuration of the controllers, services and repos. MVC model
 */
@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
class ApplicationConfig : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val jsonConverter =
            converters.find { it is MappingJackson2HttpMessageConverter } as MappingJackson2HttpMessageConverter
        jsonConverter.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        jsonConverter.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        jsonConverter.defaultCharset = Charset.forName("UTF-8")
        jsonConverter.supportedMediaTypes =
            listOf(JSON_MEDIA_TYPE, SIREN_MEDIA_TYPE, PROBLEM_JSON_MEDIA_TYPE, JSON_HOME_MEDIA_TYPE)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
    }
}

/**
 * Create the HTTP clients with base configurations for the repositories
 */
@Configuration
class HttpClientsForRepositories(
    @Autowired val env: Environment
) {
    @Bean
    fun getClient(): WebClient {
        var host = env.getProperty("orion.hostURL", "http://localhost:1026")
        return WebClient
            .builder()
            .exchangeStrategies(
                ExchangeStrategies
                    .builder()
                    .codecs
                    {
                        it
                            .defaultCodecs()
                            .maxInMemorySize(1000000)
                    }
                    .build()
            )
            .baseUrl(host)
            .build()
    }
}

/**
 * Set caching levels
 */
@Configuration
class CacheSupplier(
    @Autowired val env: Environment
) {
    @Bean
    fun getMicroCaching(): CacheControl {
        val seconds: Long  = env.getProperty("iotmapper.http.cache.microcaching", "1").toLong()
        return CacheControl.maxAge(seconds, TimeUnit.SECONDS)
            .cachePublic()
            .mustRevalidate()
    }

    @Bean
    fun getErrorCaching(): CacheControl {
        val seconds: Long  = env.getProperty("iotmapper.http.cache.errorcaching", "1").toLong()
        return CacheControl.maxAge(seconds, TimeUnit.SECONDS)
            .cachePublic()
            .mustRevalidate()
    }

    @Bean
    fun getMiniCaching(): CacheControl {
        val seconds: Long = env.getProperty("iotmapper.http.cache.minicaching", "60").toLong()
        return CacheControl.maxAge(seconds, TimeUnit.SECONDS)
            .cachePublic()
            .mustRevalidate()
    }
}

/**
 * Official main, asks Spring to create and run [IoTMapperBackendApplication]
 */
fun main(args: Array<String>) {
    runApplication<IoTMapperBackendApplication>(*args)
}
