package kr.quidev.member.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MemberDto(
    @JsonProperty("id") val id: Long?,
    @JsonProperty("name") val name: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
) {

}
