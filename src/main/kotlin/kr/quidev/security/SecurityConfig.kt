package kr.quidev.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager {
        val user = User.withDefaultPasswordEncoder()
            .username("shane")
            .password("pass")
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(user)
    }

}
