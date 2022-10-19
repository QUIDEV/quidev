package kr.quidev.security.interceptor

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import kr.quidev.common.Constants.Companion.AUTH_HEADER
import kr.quidev.common.Constants.Companion.LOGIN_TOKEN
import kr.quidev.common.Constants.Companion.REQUEST_MEMBER
import kr.quidev.member.service.MemberService
import kr.quidev.security.BcryptEncoder
import kr.quidev.security.dto.LoginToken
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginInterceptor(
    private val objectMapper: ObjectMapper,
    private val memberService: MemberService,
    private val passwordEncoder: BcryptEncoder,
) : HandlerInterceptor {

    val log = org.slf4j.LoggerFactory.getLogger(this.javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // preflight request
        if (request.method == "OPTIONS") {
            return true
        }

        try {
            val token = request.getHeader(AUTH_HEADER)
            if (token != null) {
                val decode = String(Base64.getDecoder().decode(token))
                val loginToken = objectMapper.readValue(decode, LoginToken::class.java)
                if (loginToken == null) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value())
                    log.warn("LoginInterceptor: token is null")
                    return false
                }
                val member = memberService.findByEmail(loginToken.email)

                if (passwordEncoder.matches(loginToken.password, member?.password ?: "")) {
                    request.setAttribute(REQUEST_MEMBER, member)
                    request.setAttribute(LOGIN_TOKEN, loginToken)
                    return true
                } else {
                    log.warn("LoginInterceptor: password is not matched")
                }
            }
        } catch (e: JsonParseException) {
            log.warn("LoginInterceptor: JsonParseException")
        }
        response.sendError(HttpStatus.UNAUTHORIZED.value())
        return false
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        val token: LoginToken? = request.getAttribute(LOGIN_TOKEN) as LoginToken?
        token?.let {
            token.refresh()
            val json = objectMapper.writeValueAsString(token)
            val encode = Base64.getEncoder().encodeToString(json.toByteArray())
            response.setHeader(AUTH_HEADER, encode)
        }

        super.postHandle(request, response, handler, modelAndView)
    }
}
