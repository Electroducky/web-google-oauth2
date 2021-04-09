package com.electroducky.webgoogleoauth2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.electroducky.webgoogleoauth2")
class WebGoogleOauth2Application

fun main(args: Array<String>) {
    runApplication<WebGoogleOauth2Application>(*args)
}
