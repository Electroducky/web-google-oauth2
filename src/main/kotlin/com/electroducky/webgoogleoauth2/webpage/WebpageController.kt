package com.electroducky.webgoogleoauth2.webpage

import com.electroducky.webgoogleoauth2.auth.AuthSession
import com.electroducky.webgoogleoauth2.auth.GoogleAuthService
import com.electroducky.webgoogleoauth2.person.PersonService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import javax.servlet.http.HttpSession

@Controller
class WebpageController(
    private val googleAuthService: GoogleAuthService,
    private val personService: PersonService
) : WebpageMapping {

    init {
        println("http://localhost:8080/home")
    }

    override fun home(session: HttpSession, model: Model): String {
        val authSession = getFreshAuthSession(session)

        model.addAttribute("username", personService.getMyName(authSession))
        model.addAttribute("signedIn", authSession != null)

        return "home"
    }

    override fun signin(session: HttpSession, model: Model): String {
        val authSession = getFreshAuthSession(session)

        model.addAttribute("username", personService.getMyName(authSession))
        model.addAttribute("signedIn", authSession != null)

        return "signin"
    }

    private fun getFreshAuthSession(session: HttpSession): AuthSession? {
        val authSession = session.getAttribute(AuthSession.authSessionAttribute) as AuthSession?
        val freshSession = authSession?.let { googleAuthService.freshTokenSession(it) }

        if (authSession != null && authSession != freshSession) {
            session.setAttribute(AuthSession.authSessionAttribute, authSession)
        }

        return freshSession
    }
}