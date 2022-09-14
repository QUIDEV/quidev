package kr.quidev.quiz.controller_api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kr.quidev.common.ApiResponse
import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.entity.QuizCreateDto
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.service.QuizService
import kr.quidev.quiz.service.SkillService
import kr.quidev.security.service.CustomUserDetailsService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.context.WebApplicationContext
import javax.transaction.Transactional


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class QuizApiControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    private val log = LoggerFactory.getLogger(javaClass)
    private val mapper = jacksonObjectMapper()

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var quizService: QuizService

    @Autowired
    lateinit var skillService: SkillService

    @Autowired
    lateinit var userDetailService: CustomUserDetailsService

    @Autowired
    lateinit var memberService: MemberService

    val email = "shane@park.dev"

    @BeforeEach
    fun beforeEach() {
        memberService.createMember(Member(name = "name", password = "pass", email = email))
    }

    @Test
    @DisplayName("create quiz test: expected situation")
    fun createQuiz() {
        val member = userDetailService.loadUserByUsername(email)
        val skill = skillService.save(Skill(id = null, parent = null, name = "java"))

        val description = "desc"
        val answer = "answer"
        val explanation = "explanation"
        val quizCreateDto = QuizCreateDto(
            description = description,
            answer = answer,
            explanation = explanation,
            examples = arrayOf("example1", "example2", "example3"),
            skillId = skill.id
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/quiz/new")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(member))
                .content(mapper.writeValueAsString(quizCreateDto))
        )
        result.andExpect(MockMvcResultMatchers.status().isOk)

        val content = result.andReturn().response.contentAsString
        log.info("content : {}", content)
        val response: ApiResponse = mapper.readValue(content)
        val body = response.body as Map<*, *>
        val id = body["id"].toString().toLong()

        val findById = quizService.findById(id).orElseThrow()
        assertThat(findById.answer).isEqualTo(answer)
        assertThat(findById.description).isEqualTo(description)
        assertThat(findById.explanation).isEqualTo(explanation)
        assertThat(findById.examples).hasSize(3)
        assertThat(findById.skill).isEqualTo(skill)
        assertThat(findById.skill?.name).isEqualTo("java")
    }

    @Test
    @DisplayName("create quiz test: Description is not provided")
    fun createQuizNoDesc() {
        val member = userDetailService.loadUserByUsername(email)
        for (description in arrayOf("", " ", null)) {
            val quizCreateDto = QuizCreateDto(
                description = description,
                answer = "answer",
                explanation = "explanation",
                examples = arrayOf("example1", "example2", "example3"),
                skillId = null
            )
            val result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/quiz/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.user(member))
                    .content(mapper.writeValueAsString(quizCreateDto))
            )
            result.andExpect(MockMvcResultMatchers.jsonPath("$.body").isEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))

            log.info(result.andReturn().response.contentAsString)
        }
    }

}
