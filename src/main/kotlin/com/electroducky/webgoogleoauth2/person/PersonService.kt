package com.electroducky.webgoogleoauth2.person

import com.electroducky.webgoogleoauth2.auth.AuthSession

interface PersonService {
    fun getMyName(session: AuthSession?): String?
}