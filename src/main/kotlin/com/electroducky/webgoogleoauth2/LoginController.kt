package com.electroducky.webgoogleoauth2

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.view.RedirectView
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
    fun home(session: HttpSession): String {
        val authSession = session.getAttribute(authSessionKey) as AuthSession?
        val userName = authSession?.let { loginService.getName(it) } ?: "new user"
        return """
            <p>Welcome $userName! This is a home page</p>
            <a href="/signin">Sign in</a>
            <a href="/signout">Sign out</p>
        """.trimIndent()
    }

    @GetMapping("/signin", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun signin(session: HttpSession): String {
        val authSession = session.getAttribute(authSessionKey) as AuthSession?
        val userName = authSession?.let { loginService.getName(it) } ?: "new user"
        return """
            <p>You are already signed in as $userName</p>
            <a href="/home">Home</a>
            <a href="/googlesignin">Sign In using Google Account</p>
        """.trimIndent()
    }

    @GetMapping("/googlesignin")
    fun googleSignIn(session: HttpSession): RedirectView {
        return RedirectView(loginService.getLoginRedirect(session.id))
    }

    @GetMapping("/oauthcallback")
    fun oauthcallback(
        session: HttpSession,
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) code: String?,
        @RequestParam state: String
    ): RedirectView {
        check(error == null) { error!! }

        val authSession = loginService.finishAuth(session.id, state, code!!)
        session.setAttribute(authSessionKey, authSession)
        return RedirectView("/home")
    }

    @GetMapping("/signout")
    fun signout(session: HttpSession): RedirectView {
        session.invalidate()
        return RedirectView("/home")
    }

    companion object {
        private const val authSessionKey = "authSession"
    }
}