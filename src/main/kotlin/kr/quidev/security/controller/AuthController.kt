package kr.quidev.security.controller

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class AuthController(
    private val passwordEncoder: PasswordEncoder,
) {

    @GetMapping("/join")
    fun join(): String {
        return "auth/join"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/denied")
    @ResponseBody
    fun denied(): String {
        return "denied(text)"
    }

}
