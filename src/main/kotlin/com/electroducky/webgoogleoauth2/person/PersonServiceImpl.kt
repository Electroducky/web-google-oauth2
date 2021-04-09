package com.electroducky.webgoogleoauth2.person

import com.electroducky.webgoogleoauth2.auth.AuthSession
import com.electroducky.webgoogleoauth2.person.client.GooglePersonHttpClient
import org.springframework.stereotype.Service

@Service
class PersonServiceImpl(
    private val googlePersonHttpClient: GooglePersonHttpClient
) : PersonService {
    override fun getMyName(session: AuthSession?): String? {
        session ?: return null

        val nameResponse = googlePersonHttpClient.getMyName(session.accessToken)
        return nameResponse.names.first().displayName
    }
}