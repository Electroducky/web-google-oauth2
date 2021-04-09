package com.electroducky.webgoogleoauth2.auth.client

import com.electroducky.webgoogleoauth2.clientId
import com.electroducky.webgoogleoauth2.clientSecret
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Service
class GoogleOauth2HttpClientImpl(
    private val webClient: WebClient
) : GoogleOauth2HttpClient {

    override fun exchangeAccessCodeForTokens(code: String): GoogleTokenExchangeResult {
        return webClient.post()
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
    }

    override fun refreshAccessToken(refreshToken: String): GoogleTokenRefreshResult {
        return webClient.post()
            .uri("https://oauth2.googleapis.com/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData("client_id", clientId)
                    .with("client_secret", clientSecret)
                    .with("refresh_token", refreshToken)
                    .with("grant_type", "refresh_token")
            )
            .retrieve()
            .bodyToMono(GoogleTokenRefreshResult::class.java)
            .block()!!
    }
}