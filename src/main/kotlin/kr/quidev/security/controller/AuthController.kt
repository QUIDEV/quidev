package kr.quidev.security.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class AuthController {

    @GetMapping("login")
    fun login(): String {
        return "login"
    }

    @GetMapping("denied")
    @ResponseBody
    fun denied(): String {
        return "denied(text)"
    }

}
