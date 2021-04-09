package com.electroducky.webgoogleoauth2.secret

import com.electroducky.webgoogleoauth2.common.WebProperties
import com.electroducky.webgoogleoauth2.common.logger
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class SecretConfiguration {
    private val log = logger()

    @Bean
    fun googleApiProperties(
        objectMapper: ObjectMapper,
        webProperties: WebProperties,
        @Value("\${app.google.secret.path}") path: File
    ): GoogleApiProperties {
        if (!path.isDirectory) {
            throw IllegalStateException("Secret path $path is not a directory")
        }

        val secretFile =
            path.walk().filter { it.extension == "json" }.filter { it.name.startsWith("client_secret") }.firstOrNull()
                ?: throw IllegalStateException("Secret path $path does not contain json files that start with 'client_secret'")
        log.info("Loading google api configuration from $secretFile")
        val properties = try {
            objectMapper.readValue<GoogleApiProperties>(secretFile)
        } catch (e: JsonParseException) {
            throw IllegalStateException("Can't parse secret file $secretFile")
        } catch (e: JsonMappingException) {
            throw IllegalStateException("Can't parse secret file $secretFile")
        }

        check(webProperties.oauthCallback in properties.web.redirectUris) {
            """
            
            
            Current server root is not registered as redirect url in loaded google configuration. 
            Edit server url in application.properties or register new redirectUri ${webProperties.oauthCallback} in Google Cloud Console:
            https://console.cloud.google.com/apis/api/people.googleapis.com/credentials?project=${properties.web.projectId}
            
            """
        }

        return properties
    }
}