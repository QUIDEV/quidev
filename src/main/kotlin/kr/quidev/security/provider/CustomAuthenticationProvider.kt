package kr.quidev.security.provider

import kr.quidev.security.service.CustomUserDetailsService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder

class CustomAuthenticationProvider(
    private val passwordEncoder: PasswordEncoder,
    private val customUserDetailsService: CustomUserDetailsService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        authentication?.let {
            return authentication(authentication)
        } ?: run {
            throw RuntimeException("authentication is null")
        }
    }

    private fun authentication(authentication: Authentication): Authentication {
        val username = authentication.name
        val beforePass = authentication.credentials.toString()

        customUserDetailsService.loadUserByUsername(username)?.let {
            if (passwordEncoder.matches(beforePass, it.password)) {
                return authentication
            } else {
                throw RuntimeException("password is not matched")
            }
        }
        throw RuntimeException("user not found")
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return true
    }
}
