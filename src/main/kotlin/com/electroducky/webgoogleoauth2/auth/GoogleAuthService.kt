package com.electroducky.webgoogleoauth2.auth

interface GoogleAuthService {
    fun beginAuthUrl(sessionId: String): String
    fun finishAuth(sessionId: String, state: String, code: String): AuthSession
}