package com.electroducky.webgoogleoauth2.webpage

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpSession

interface WebpageMapping {
    @GetMapping("/home")
    fun home(session: HttpSession, model: Model): String

    @GetMapping("/signin")
    fun signin(session: HttpSession, model: Model): String
}