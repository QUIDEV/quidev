package kr.quidev.security.dto

import java.time.LocalDateTime

class LoginToken(
    val email: String,
    val password: String,
    var issuedAt: LocalDateTime
) {
    fun refresh() {
        issuedAt = LocalDateTime.now()
    }

    override fun toString(): String {
        return "LoginToken(email='$email', password='$password', issuedAt=$issuedAt)"
    }


}
