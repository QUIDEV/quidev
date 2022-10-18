package kr.quidev.security.controller

import kr.quidev.security.BcryptEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class AuthController(
    private val passwordEncoder: BcryptEncoder,
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
