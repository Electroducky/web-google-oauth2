package com.electroducky.webgoogleoauth2.auth.client

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenRefreshResult(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
    @JsonProperty("scope")
    val scope: String,
    @JsonProperty("token_type")
    val tokenType: String
)