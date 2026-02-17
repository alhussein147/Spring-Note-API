package com.hussein.notes_api.controllers.auth

import com.hussein.notes_api.security.AuthService
import com.hussein.notes_api.security.TokenPair
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/auth")
class AuthController(val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody authRequest: AuthRequest) {
        authService.register(email = authRequest.email, password = authRequest.password)
    }


    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest):TokenPair {
        return authService.login(email = authRequest.email, password = authRequest.password)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody refreshRequest: RefreshRequest): TokenPair {
        return authService.refresh(refreshToken = refreshRequest.refreshToken)
    }



}