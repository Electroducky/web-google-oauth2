package com.electroducky.webgoogleoauth2.auth.client

import com.electroducky.webgoogleoauth2.common.WebProperties
import com.electroducky.webgoogleoauth2.secret.GoogleApiProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Service
class GoogleOauth2HttpClientImpl(
    private val webClient: WebClient,
    private val googleApiProperties: GoogleApiProperties,
    private val webProperties: WebProperties
) : GoogleOauth2HttpClient {

    override fun exchangeAccessCodeForTokens(code: String): GoogleTokenExchangeResult {
        return webClient.post()
            .uri(googleApiProperties.web.tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData("client_id", googleApiProperties.web.clientId)
                    .with("client_secret", googleApiProperties.web.clientSecret)
                    .with("code", code)
                    .with("grant_type", "authorization_code")
                    .with("redirect_uri", webProperties.oauthCallback)
            )
            .retrieve()
            .bodyToMono(GoogleTokenExchangeResult::class.java)
            .block()!!
    }

    override fun refreshAccessToken(refreshToken: String): GoogleTokenRefreshResult {
        return webClient.post()
            .uri(googleApiProperties.web.tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData("client_id", googleApiProperties.web.clientId)
                    .with("client_secret", googleApiProperties.web.clientSecret)
                    .with("refresh_token", refreshToken)
                    .with("grant_type", "refresh_token")
            )
            .retrieve()
            .bodyToMono(GoogleTokenRefreshResult::class.java)
            .block()!!
    }
}