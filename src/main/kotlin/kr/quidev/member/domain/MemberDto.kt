package kr.quidev.member.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class MemberDto(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
) {

}
