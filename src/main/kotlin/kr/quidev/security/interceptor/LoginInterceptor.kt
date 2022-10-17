package kr.quidev.security.interceptor

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import kr.quidev.member.service.MemberService
import kr.quidev.security.BcryptEncoder
import kr.quidev.security.dto.LoginDto
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginInterceptor(
    private val objectMapper: ObjectMapper,
    private val memberService: MemberService,
    private val passwordEncoder: BcryptEncoder,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        try {
            val token = request.getHeader("auth")
            if (token != null) {
                val decode = String(Base64.getDecoder().decode(token))
                val loginDto = objectMapper.readValue(decode, LoginDto::class.java)
                if (loginDto == null) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value())
                    return false
                }
                val member = memberService.findByEmail(loginDto.email)

                if (passwordEncoder.matches(loginDto.password, member?.password ?: "")) {
                    request.setAttribute("member", member)
                    return true
                }
            }
        } catch (e: JsonParseException) {

        }
        response.sendError(HttpStatus.UNAUTHORIZED.value())
        return false
    }

}
