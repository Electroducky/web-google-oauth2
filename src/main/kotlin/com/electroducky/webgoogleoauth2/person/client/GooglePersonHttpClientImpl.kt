package com.electroducky.webgoogleoauth2.person.client

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GooglePersonHttpClientImpl(private val webClient: WebClient) : GooglePersonHttpClient {

    override fun getMyName(accessToken: String): GooglePerson {
        return webClient.get()
            .uri("https://people.googleapis.com/v1/people/me?personFields=names")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono(GooglePerson::class.java)
            .block()!!
    }
}