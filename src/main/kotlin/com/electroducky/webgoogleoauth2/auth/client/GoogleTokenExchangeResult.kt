package com.electroducky.webgoogleoauth2.auth.client

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenExchangeResult(
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