package com.electroducky.webgoogleoauth2

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class LoginController {
    init {
        println("http://localhost:8080/home")
    }

    @GetMapping("/home", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun home(): String = """
        <p>Welcome new user! This is a home page</p>
        <a href="/signin">Sign in</a>
        <a href="/signout">Sign out</p>
    """.trimIndent()

    @GetMapping("/signin", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun signin(): String = """
        <p>You are already signed in as username</p>
        <a href="/home">Home</a>
        <a href="/oauth2">Sign In using Google Account</p>
    """.trimIndent()
}