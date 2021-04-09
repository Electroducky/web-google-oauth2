package com.electroducky.webgoogleoauth2.secret

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleWebProperties(
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("auth_uri")
    val authUri: String,
    @JsonProperty("project_id")
    val projectId: String,
    @JsonProperty("token_uri")
    val tokenUri: String,
    @JsonProperty("client_secret")
    val clientSecret: String,
    @JsonProperty("redirect_uris")
    val redirectUris: List<String>
)


data class GoogleApiProperties(
    @JsonProperty("web")
    val web: GoogleWebProperties
)