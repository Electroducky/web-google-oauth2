package com.electroducky.webgoogleoauth2

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpSession

@Controller
class LoginController(
    private val loginService: LoginService
) {
    init {
        println("http://localhost:8080/home")
    }

    @GetMapping("/home")
    fun home(session: HttpSession, model: Model): String {
        val authSession = session.getAttribute(authSessionKey) as AuthSession?

        model.addAttribute("username", loginService.getName(authSession))
        model.addAttribute("signedIn", authSession != null)

        return "home"
    }

    @GetMapping("/signin")
    fun signin(session: HttpSession, model: Model): String {
        val authSession = session.getAttribute(authSessionKey) as AuthSession?

        model.addAttribute("username", loginService.getName(authSession))
        model.addAttribute("signedIn", authSession != null)

        return "signin"
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