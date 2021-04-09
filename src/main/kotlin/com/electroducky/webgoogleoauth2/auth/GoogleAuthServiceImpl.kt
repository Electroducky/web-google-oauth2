package com.electroducky.webgoogleoauth2.auth

import com.electroducky.webgoogleoauth2.auth.client.GoogleOauth2HttpClient
import com.electroducky.webgoogleoauth2.common.WebProperties
import com.electroducky.webgoogleoauth2.common.logger
import com.electroducky.webgoogleoauth2.secret.GoogleApiProperties
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration
import java.time.Instant

@Service
class GoogleAuthServiceImpl(
    private val googleApiProperties: GoogleApiProperties,
    private val googleOauth2HttpClient: GoogleOauth2HttpClient,
    private val webProperties: WebProperties
) : GoogleAuthService {
    private val log = logger()

    override fun beginAuthUrl(sessionId: String): String {
        log.info("Begin OAuth2 flow for session $sessionId")

        return UriComponentsBuilder.fromPath(googleApiProperties.web.authUri)
            .queryParam("client_id", googleApiProperties.web.clientId)
            .queryParam("redirect_uri", webProperties.oauthCallback)
            .queryParam("response_type", "code")
            .queryParam("access_type", "offline")
            .queryParam("scope", "https://www.googleapis.com/auth/userinfo.profile")
            .queryParam("state", sessionId)
            .queryParam("prompt", "consent")
            .build()
            .encode()
            .toUriString()
    }

    override fun finishAuth(sessionId: String, state: String, code: String): AuthSession {
        if (sessionId != state) {
            log.warn("State mismatch for OAuth2 flow actual state: $state, expected: $sessionId")
            throw IllegalStateException("Broken state please try again")
        }

        val tokenData = googleOauth2HttpClient.exchangeAccessCodeForTokens(code)
        log.info("Finished OAuth2 flow for session $sessionId")

        return AuthSession(
            tokenData.accessToken,
            Instant.now().plusSeconds(tokenData.expiresIn.toLong()),
            tokenData.refreshToken
        )
    }

    override fun freshTokenSession(session: AuthSession): AuthSession {
        if (Duration.between(Instant.now(), session.expirationTimestamp).seconds > 100)
            return session

        log.info("Access token expires on ${session.expirationTimestamp}, refreshing")
        val refreshedToken = googleOauth2HttpClient.refreshAccessToken(session.refreshToken)
        return AuthSession(
            refreshedToken.accessToken,
            Instant.now().plusSeconds(refreshedToken.expiresIn.toLong()),
            session.refreshToken
        )
    }
}