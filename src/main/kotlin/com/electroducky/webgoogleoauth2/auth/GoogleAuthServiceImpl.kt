package com.electroducky.webgoogleoauth2.auth

import com.electroducky.webgoogleoauth2.auth.client.GoogleTokenExchangeResult
import com.electroducky.webgoogleoauth2.clientId
import com.electroducky.webgoogleoauth2.clientSecret
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import java.time.Instant

@Service
class GoogleAuthServiceImpl : GoogleAuthService {
    private val webClient = WebClient.create()

    override fun beginAuthUrl(sessionId: String): String {
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

    override fun finishAuth(sessionId: String, state: String, code: String): AuthSession {
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
            .bodyToMono(GoogleTokenExchangeResult::class.java)
            .block()!!

        return AuthSession(
            tokens.accessToken,
            Instant.now().plusSeconds(tokens.expiresIn.toLong()),
            tokens.refreshToken
        )
    }
}