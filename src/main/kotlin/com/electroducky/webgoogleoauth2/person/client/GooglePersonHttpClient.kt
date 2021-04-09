package com.electroducky.webgoogleoauth2.person.client

interface GooglePersonHttpClient {
    fun getMyName(accessToken: String): GooglePerson
}