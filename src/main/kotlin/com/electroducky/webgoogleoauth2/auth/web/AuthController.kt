package com.electroducky.webgoogleoauth2.auth.web

import com.electroducky.webgoogleoauth2.auth.AuthSession
import com.electroducky.webgoogleoauth2.auth.GoogleAuthService
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpSession

@Controller
class AuthController(private val googleAuthService: GoogleAuthService) : AuthMapping {
    override fun googleSignIn(session: HttpSession): RedirectView {
        return RedirectView(googleAuthService.beginAuthUrl(session.id))
    }

    override fun signout(session: HttpSession): RedirectView {
        session.invalidate()
        return RedirectView("/home")
    }

    override fun oauthcallback(session: HttpSession, error: String?, code: String?, state: String): RedirectView {
        if (error != null) {
            throw IllegalStateException(error)
        }

        val authSession = googleAuthService.finishAuth(session.id, state, code!!)
        session.setAttribute(AuthSession.authSessionAttribute, authSession)
        return RedirectView("/home")
    }
}