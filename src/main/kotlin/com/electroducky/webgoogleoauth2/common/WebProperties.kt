package com.electroducky.webgoogleoauth2.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.UriComponentsBuilder

@ConstructorBinding
@ConfigurationProperties(prefix = "app.web")
class WebProperties(val scheme: String, val hostname: String, val port: Int, val oauthcallbackPath: String) {
    val oauthCallback = UriComponentsBuilder.newInstance()
        .scheme(scheme)
        .host(hostname)
        .port(port)
        .path(oauthcallbackPath)
        .toUriString()

    val rootBuilder
        get() = UriComponentsBuilder.newInstance()
            .scheme(scheme)
            .host(hostname)
            .port(port)
}