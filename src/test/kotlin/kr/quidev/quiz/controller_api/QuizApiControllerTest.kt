package kr.quidev.quiz.controller_api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kr.quidev.common.dto.ApiResponse
import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.repository.QuizRepository
import kr.quidev.quiz.service.QuizService
import kr.quidev.quiz.service.SkillService
import kr.quidev.security.service.CustomUserDetailsService
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import javax.transaction.Transactional


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @Autowired
    lateinit var quizRepository: QuizRepository

    val email = "shane@park.dev"
    private var member: Member? = null
    private var skill: Skill? = null

    @BeforeAll
    fun beforeAll() {
        member = memberService.createMember(Member(name = "name", password = "pass", email = email))
        skill = skillService.save(Skill(id = null, parent = null, name = "java"))
    }

    @BeforeEach
    fun beforeEach() {
        quizRepository.deleteAll()
    }

    @Test
    @DisplayName("create quiz test: expected situation")
    fun createQuiz() {
        val user = userDetailService.loadUserByUsername(email)
        val description = "desc"
        val answer = "answer"
        val explanation = "explanation"
        val quizCreateDto = QuizCreateDto(
            description = description,
            answer = answer,
            explanation = explanation,
            examples = arrayOf("example1", "example2", "example3"),
            skillId = skill!!.id
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/quiz")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(user))
                .content(mapper.writeValueAsString(quizCreateDto))
        )
        result.andExpect(status().isOk)

        val content = result.andReturn().response.contentAsString
        log.info("content : {}", content)
        val response: ApiResponse = mapper.readValue(content)
        val body = response.body as Map<*, *>
        val id = body["id"].toString().toLong()

        val findById = quizService.findById(id)
        assertThat(findById.answer).isEqualTo(answer)
        assertThat(findById.description).isEqualTo(description)
        assertThat(findById.explanation).isEqualTo(explanation)
        assertThat(findById.examples).hasSize(3)
        assertThat(findById.skill?.name).isEqualTo("java")
    }

    @Test
    @DisplayName("create quiz test: Description is not provided")
    fun createQuizNoDesc() {
        val user = userDetailService.loadUserByUsername(email)
        for (description in arrayOf("", " ", null)) {
            val quizCreateDto = QuizCreateDto(
                description = description,
                answer = "answer",
                explanation = "explanation",
                examples = arrayOf("example1", "example2", "example3"),
                skillId = null
            )
            val result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/quiz")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.user(user))
                    .content(mapper.writeValueAsString(quizCreateDto))
            )
            result.andExpect(jsonPath("$.body").isEmpty)
                .andExpect(jsonPath("$.error").isNotEmpty)
                .andExpect(jsonPath("$.error.code").value("400"))

            log.info(result.andReturn().response.contentAsString)
        }
    }

    @Test
    fun findQuizTest() {
        // Given
        val description = "desc"
        val quiz = quizService.createQuiz(
            submitter = member!!, createDto = QuizCreateDto(
                description = description,
                answer = "answer",
                explanation = "explanation",
                examples = arrayOf("example1", "example2", "example3"),
                skillId = skill!!.id
            )
        )

        // Expected
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/quiz/{id}", quiz.id)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("randomUser"))
        )
        log.info("response: {}", result.andReturn().response.contentAsString)
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.body.id").value(quiz.id))
            .andExpect(jsonPath("$.body.description").value(description))
            .andExpect(jsonPath("$.body.answer").value(quiz.answer))
    }

    @Test
    fun findAllQuizTest() {
        // Given
        val quizzes = mutableListOf<Quiz>()
        val size = 50
        for (i in 1..size) {
            quizzes.add(
                quizService.createQuiz(
                    submitter = member!!, createDto = QuizCreateDto(
                        description = "desc$i",
                        answer = "answer$i",
                        explanation = "explanation$i",
                        examples = arrayOf("example1", "example2", "example3"),
                        skillId = skill!!.id
                    )
                )
            )
        }

        // Expected
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/quiz")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("randomUser"))
        )
        log.info("response: {}", result.andReturn().response.contentAsString)
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.body.totalElements").value(size))
            .andExpect(jsonPath("$.body.content").isArray)
            .andExpect(jsonPath("$.body.content.length()", `is`(10)))
            .andExpect(jsonPath("$.body.content[0].id").value(quizzes[0].id))
            .andExpect(jsonPath("$.body.content[0].answer").value(quizzes[0].answer))
            .andExpect(jsonPath("$.body.content[1].id").value(quizzes[1].id))
            .andExpect(jsonPath("$.body.content[1].explanation").value(quizzes[1].explanation))
    }

    @Test
    fun searchQuizTest() {
        // Given
        val quizzes = mutableListOf<Quiz>()
        val size = 50
        for (i in 1..size) {
            quizzes.add(
                quizService.createQuiz(
                    submitter = member!!, createDto = QuizCreateDto(
                        description = "desc$i",
                        answer = "answer$i",
                        explanation = "explanation$i",
                        examples = arrayOf("example1", "example2", "example3"),
                        skillId = skill!!.id
                    )
                )
            )
        }

        // Expected
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/quiz/search")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("randomUser"))
        )
        log.info("response: {}", result.andReturn().response.contentAsString)
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.body").isArray)
            .andExpect(jsonPath("$.body.length()", `is`(10)))
            .andExpect(jsonPath("$.body[0].id").value(quizzes[49].id))
            .andExpect(jsonPath("$.body[0].answer").value(quizzes[49].answer))
            .andExpect(jsonPath("$.body[1].id").value(quizzes[48].id))
            .andExpect(jsonPath("$.body[1].explanation").value(quizzes[48].explanation))
    }

}
