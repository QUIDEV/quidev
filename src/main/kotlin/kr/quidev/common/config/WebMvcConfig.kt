package kr.quidev.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import kr.quidev.member.service.MemberService
import kr.quidev.security.BcryptEncoder
import kr.quidev.security.interceptor.LoginInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val objectMapper: ObjectMapper,
    private val memberService: MemberService,
    private val passwordEncoder: BcryptEncoder,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LoginInterceptor(objectMapper, memberService, passwordEncoder))
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/login")
    }

}
