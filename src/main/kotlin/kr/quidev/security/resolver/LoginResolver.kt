package kr.quidev.security.resolver

import kr.quidev.common.Constants.Companion.REQUEST_MEMBER
import kr.quidev.member.domain.entity.Member
import kr.quidev.security.annotation.LoginMember
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class LoginResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request: HttpServletRequest = webRequest.nativeRequest as HttpServletRequest
        val member = request.getAttribute(REQUEST_MEMBER) as Member

        val loginMember: LoginMember? = parameter.getParameterAnnotation(LoginMember::class.java)

        if (member == null || loginMember == null) {
            throw IllegalArgumentException("No member")
        }

        return member

    }
}
