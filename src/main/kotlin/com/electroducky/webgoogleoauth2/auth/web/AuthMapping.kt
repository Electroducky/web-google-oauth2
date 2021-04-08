package com.electroducky.webgoogleoauth2.auth.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpSession

interface AuthMapping {
    @GetMapping("/googlesignin")
    fun googleSignIn(session: HttpSession): RedirectView

    @GetMapping("/signout")
    fun signout(session: HttpSession): RedirectView

    @GetMapping("/oauthcallback")
    fun oauthcallback(
        session: HttpSession,
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) code: String?,
        @RequestParam state: String
    ): RedirectView
}