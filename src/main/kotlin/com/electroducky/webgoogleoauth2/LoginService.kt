package com.electroducky.webgoogleoauth2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import java.time.Instant

@Service
class LoginService {
    private val webClient = WebClient.create()

    fun getLoginRedirect(sessionId: String): String {
        return UriComponentsBuilder.fromPath("https://accounts.google.com/o/oauth2/auth")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", "http://localhost:8080/oauthcallback")
            .queryParam("response_type", "code")
            .queryParam("access_type", "offline")
            .queryParam("scope", "https://www.googleapis.com/auth/userinfo.profile")
            .queryParam("state", sessionId)
            .build()
            .encode()
            .toUriString()
    }

    fun finishAuth(sessionId: String, state: String, code: String): AuthSession {
        check(sessionId == state) { "Broken state please try again" }

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

        return AuthSession(
            tokens.accessToken,
            Instant.now().plusSeconds(tokens.expiresIn.toLong()),
            tokens.refreshToken
        )
    }

    fun getName(authSession: AuthSession): String {
        val person = webClient.get()
            .uri("https://people.googleapis.com/v1/people/me?personFields=names")
            .header("Authorization", "Bearer ${authSession.accessToken}")
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