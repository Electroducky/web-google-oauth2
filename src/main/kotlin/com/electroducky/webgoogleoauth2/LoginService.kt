package com.electroducky.webgoogleoauth2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Service
class LoginService {
    private val webClient = WebClient.create()

    fun getName(code: String): String {
        val tokens = webClient.post()
            .uri("https://oauth2.googleapis.com/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData("client_id", clientId)
                    .with("client_secret", clientSecret)
                    .with("code", code)
                    .with("grant_type", "authorization_code")
                    .with("redirect_uri", "http://localhost:8080/oauthcallback")
            )
            .retrieve()
            .bodyToMono(Tokens::class.java)
            .block()!!

        val person = webClient.get()
            .uri("https://people.googleapis.com/v1/people/me?personFields=names")
            .header("Authorization", "Bearer ${tokens.accessToken}")
            .retrieve()
            .bodyToMono(Person::class.java)
            .block()!!

        return person.names.first().displayName
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class PersonName(val displayName: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Person(val names: List<PersonName>)

data class Tokens(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("scope")
    val scope: String,
    @JsonProperty("token_type")
    val tokenType: String
)