package com.electroducky.webgoogleoauth2.person

import com.electroducky.webgoogleoauth2.auth.AuthSession
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class PersonServiceImpl : PersonService {
    private val webClient = WebClient.create()

    override fun getMyName(session: AuthSession?): String? {
        session ?: return null

        val person = webClient.get()
            .uri("https://people.googleapis.com/v1/people/me?personFields=names")
            .header("Authorization", "Bearer ${session.accessToken}")
            .retrieve()
            .bodyToMono(GooglePerson::class.java)
            .block()!!

        return person.names.first().displayName
    }
}