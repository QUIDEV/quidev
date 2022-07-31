package kr.quidev.security

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.RequestCache

@Configuration
@EnableWebSecurity
class SecurityConfig(private val userDetailsService: UserDetailsService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeRequests()
            .antMatchers("/adm")
            .access("hasRole('ADMIN')")
            .antMatchers("/css/**").permitAll()
            .antMatchers("/", "/join")
            .permitAll()
            .anyRequest()
            .authenticated()

        http.formLogin()
            .defaultSuccessUrl("/")
            .usernameParameter("username")
            .passwordParameter("password")
            .loginProcessingUrl("/login")
            .successHandler { request, response, authentication ->
                val requestCache: RequestCache = HttpSessionRequestCache()
                val savedRequest = requestCache.getRequest(request, response)
                response.sendRedirect(savedRequest.redirectUrl)
                log.info("login succeed. authentication : $authentication")
            }.failureHandler { request, response, exception ->
                log.info("login failed. exception: ${exception.message}")
                response.sendRedirect("/login")
            }.permitAll()

        http.logout()
            .logoutUrl("/logout")
            .addLogoutHandler { request, response, authentication ->
                log.info("running logout handler. authentication : $authentication")
                val session = request.session
                session.invalidate()
            }.logoutSuccessHandler { _, response, authentication ->
                response.sendRedirect("login")
                log.info("running logout success handler")
            }.deleteCookies("remember-me")

        http.rememberMe()
            .rememberMeParameter("remember_me")
            .tokenValiditySeconds(60 * 60 * 24) // default : 14 days
            .alwaysRemember(false) // remember me activated only when it's checked
            .userDetailsService(userDetailsService)

        http.sessionManagement()
            .maximumSessions(2)
            .maxSessionsPreventsLogin(false) // expires old ones.

        http.exceptionHandling()
            .accessDeniedHandler { request, response, accessDeniedException ->
                response.sendRedirect("/denied")
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

}
