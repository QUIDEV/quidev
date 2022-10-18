package kr.quidev

import com.fasterxml.jackson.databind.ObjectMapper
import kr.quidev.member.service.MemberService
import kr.quidev.security.BcryptEncoder
import kr.quidev.security.interceptor.LoginInterceptor
import kr.quidev.security.resolver.LoginResolver
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class QuidevApplication(
    private val objectMapper: ObjectMapper,
    private val memberService: MemberService,
    private val passwordEncoder: BcryptEncoder,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LoginInterceptor(objectMapper, memberService, passwordEncoder))
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/login")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(LoginResolver())
    }
}

fun main(args: Array<String>) {
    runApplication<QuidevApplication>(*args)
}
