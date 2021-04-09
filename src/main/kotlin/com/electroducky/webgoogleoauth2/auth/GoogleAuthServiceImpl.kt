package com.electroducky.webgoogleoauth2.auth

import com.electroducky.webgoogleoauth2.auth.client.GoogleOauth2HttpClient
import com.electroducky.webgoogleoauth2.clientId
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration
import java.time.Instant

@Service
class GoogleAuthServiceImpl(
    private val googleOauth2HttpClient: GoogleOauth2HttpClient
) : GoogleAuthService {

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
        if (sessionId != state) {
            throw IllegalStateException("Broken state please try again")
        }

        val tokenData = googleOauth2HttpClient.exchangeAccessCodeForTokens(code)
        return AuthSession(
            tokenData.accessToken,
            Instant.now().plusSeconds(tokenData.expiresIn.toLong()),
            tokenData.refreshToken
        )
    }

    override fun freshTokenSession(session: AuthSession): AuthSession {
        if (Duration.between(Instant.now(), session.expirationTimestamp).seconds > 100)
            return session

        val refreshedToken = googleOauth2HttpClient.refreshAccessToken(session.refreshToken)
        return AuthSession(
            refreshedToken.accessToken,
            Instant.now().plusSeconds(refreshedToken.expiresIn.toLong()),
            session.refreshToken
        )
    }
}