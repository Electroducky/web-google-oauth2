package com.electroducky.webgoogleoauth2.common

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfiguration {
    @Bean
    fun webClient(): WebClient {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT.toInt())
            .responseTimeout(Duration.ofMillis(TIMEOUT))
            .doOnConnected { connection ->
                connection
                    .addHandlerLast(ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
            }

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }

    companion object {
        private const val TIMEOUT = 10000L
    }
}