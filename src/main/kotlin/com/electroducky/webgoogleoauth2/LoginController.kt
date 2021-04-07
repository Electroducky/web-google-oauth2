package com.electroducky.webgoogleoauth2

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.view.RedirectView
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpSession

@Controller
class LoginController(
    private val loginService: LoginService
) {
    init {
        println("http://localhost:8080/home")
    }

    @GetMapping("/home", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun home(@RequestParam("name", required = false) name: String?): String = """
        <p>Welcome ${name ?: "new user"}! This is a home page</p>
        <a href="/signin">Sign in</a>
        <a href="/signout">Sign out</p>
    """.trimIndent()

    @GetMapping("/signin", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun signin(): String {
        val googleSignInUrl = UriComponentsBuilder.fromPath("https://accounts.google.com/o/oauth2/auth")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", "http://localhost:8080/oauthcallback")
            .queryParam("response_type", "code")
            .queryParam("access_type", "offline")
            .queryParam("scope", "https://www.googleapis.com/auth/userinfo.profile")
            .queryParam("state", "0")
            .build()
            .encode()
            .toUriString()

        return """
            <p>You are already signed in as username</p>
            <a href="/home">Home</a>
            <a href="$googleSignInUrl">Sign In using Google Account</p>
        """.trimIndent()
    }

    @GetMapping("/oauthcallback")
    fun oauthcallback(
        session: HttpSession,
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) code: String?,
        @RequestParam state: String
    ): RedirectView {
        return RedirectView("/home?name=${loginService.getName(code!!)}")
    }
}