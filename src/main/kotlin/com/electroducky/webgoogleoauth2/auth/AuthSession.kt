package com.electroducky.webgoogleoauth2.auth

import java.io.Serializable
import java.time.Instant

data class AuthSession(
    val accessToken: String,
    val expirationTimestamp: Instant,
    val refreshToken: String
) : Serializable {

    companion object {
        private const val serialVersionUID: Long = 1
        const val authSessionAttribute = "authSession"
    }
}
