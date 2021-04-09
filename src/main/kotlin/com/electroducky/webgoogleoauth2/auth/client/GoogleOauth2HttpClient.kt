package com.electroducky.webgoogleoauth2.auth.client

interface GoogleOauth2HttpClient {
    fun exchangeAccessCodeForTokens(code: String): GoogleTokenExchangeResult

    fun refreshAccessToken(refreshToken: String): GoogleTokenRefreshResult
}