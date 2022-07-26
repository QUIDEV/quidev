package kr.quidev.security.interceptor

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.quidev.common.Constants.*
import kr.quidev.common.Constants.Companion.AUTH_HEADER
import kr.quidev.member.domain.entity.Member
import kr.quidev.member.repository.MemberRepository
import kr.quidev.security.BcryptEncoder
import kr.quidev.security.dto.LoginToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
internal class LoginInterceptorTest {

    private val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var passwordEncoder: BcryptEncoder

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeAll
    fun beforeAll() {
        val userName = "shane"
        val password = "1234"
        val email = "shane"
        var member = memberRepository.findMemberByEmail(userName).orElse(null)
        if (member == null) {
            member = Member(name = userName, password = passwordEncoder.encode(password), email = email)
        }
        memberRepository.save(member)
    }

    @Test
    @DisplayName("without authentication header")
    fun testNonHeader() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/quiz")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(401))
    }

    @Test
    @DisplayName("invalid token")
    fun invalidToken() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/quiz")
                .header(AUTH_HEADER, "invalid")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(401))
            .andExpect(header().doesNotExist(AUTH_HEADER))
    }

    @Test
    @DisplayName("base64 test")
    fun base64() {
        val original = "original"
        val encoded = Base64.getEncoder().encodeToString(original.toByteArray())
        val decoded = String(Base64.getDecoder().decode(encoded))
        assertEquals(original, decoded)
    }

    @Test
    @DisplayName("good token")
    fun tokenOk() {
        val loginToken = LoginToken("shane", "1234", LocalDateTime.now())
        val json = mapper.writeValueAsString(loginToken)
        val token = Base64.getEncoder().encodeToString(json.toByteArray()).toString()
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/quiz")
                .header(AUTH_HEADER, token)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(200))
            .andExpect(header().exists(AUTH_HEADER))
    }

}
