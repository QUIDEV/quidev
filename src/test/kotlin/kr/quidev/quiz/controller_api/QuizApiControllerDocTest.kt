package kr.quidev.quiz.controller_api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.quidev.common.TestUtils.Companion.randomString
import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.dto.QuizUpdateDto
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.repository.QuizRepository
import kr.quidev.quiz.service.QuizService
import kr.quidev.quiz.service.SkillService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.quizdev.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class QuizApiControllerDocTest {

    private val mapper = jacksonObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var skillService: SkillService

    @Autowired
    lateinit var quizService: QuizService

    @Autowired
    lateinit var quizRepository: QuizRepository

    private var member: Member? = null
    private var skill: Skill? = null

    @BeforeAll
    fun beforeAll() {
        member = memberService.createMember(Member(name = randomString(), password = "pass", email = randomString()))
        skill = skillService.save(Skill(id = null, parent = null, name = "java"))
    }

    @BeforeEach
    fun beforeEach() {
        quizRepository.deleteAll()
    }

    @Test
    @DisplayName("retrieve all quizzes")
    fun allQuizTest() {
        for (i in 1..50) {
            quizService.createQuiz(
                submitter = member!!, createDto = QuizCreateDto(
                    description = "desc$i",
                    answer = "answer$i",
                    explanation = "explanation$i",
                    examples = arrayOf("example1", "example2", "example3"),
                    skillId = skill!!.id
                )
            )
        }

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/api/quiz")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "quiz-all",
                    responseFields(
                        fieldWithPath("body.content[].id").description("quiz id"),
                        fieldWithPath("body.content[].description").description("quiz description"),
                        fieldWithPath("body.content[].answer").description("quiz answer"),
                        fieldWithPath("body.content[].explanation").description("quiz explanation"),
                        fieldWithPath("body.content[].examples").description("quiz examples"),
                        fieldWithPath("body.content[].skill").description("quiz skill name"),
                        fieldWithPath("body.content[].submitterName").description("quiz submitter name"),
                        fieldWithPath("body.pageNo").description("page number"),
                        fieldWithPath("body.pageSize").description("page size"),
                        fieldWithPath("body.totalElements").description("total elements"),
                        fieldWithPath("body.totalPages").description("total pages"),
                        fieldWithPath("error").description("error"),
                    )
                )
            )
    }

    @Test
    @DisplayName("retrieve one quiz")
    fun findOneQuizTest() {
        val quiz = quizService.createQuiz(
            submitter = member!!, createDto = QuizCreateDto(
                description = "desc",
                answer = "answer",
                explanation = "explanation",
                examples = arrayOf("example1", "example2", "example3"),
                skillId = skill!!.id
            )
        )

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/quiz/{quizId}", quiz.id)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "quiz-inquiry",
                    pathParameters(
                        parameterWithName("quizId").description("Id of the quiz")
                    ),
                    responseFields(
                        fieldWithPath("body.id").description("Quiz id"),
                        fieldWithPath("body.description").description("Quiz description"),
                        fieldWithPath("body.answer").description("Quiz answer"),
                        fieldWithPath("body.explanation").description("Quiz explanation"),
                        fieldWithPath("body.examples").description("Quiz examples"),
                        fieldWithPath("body.skill").description("Skill field of the Quiz"),
                        fieldWithPath("body.submitterName").description("Quiz Submitter"),
                        fieldWithPath("error").description("Error Response"),
                    )
                )
            )
    }

    @Test
    @DisplayName("Create Quiz")
    fun createQuizTest() {
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

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/quiz")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(quizCreateDto))
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "quiz-create",
                    requestFields(
                        fieldWithPath("description").description("Quiz description")
                            .attributes(
                                Attributes.key("constraint").value("Description must be between 10 and 5000 characters")
                            ),
                        fieldWithPath("answer").description("Quiz answer"),
                        fieldWithPath("explanation").description("Quiz explanation"),
                        fieldWithPath("examples").description("Quiz examples").optional(),
                        fieldWithPath("skillId").description("Skill id of the Quiz"),
                    )
                )
            )
    }

    @Test
    @DisplayName("Update Quiz")
    fun editQuizTest() {
        // Given
        val quiz = quizService.createQuiz(
            submitter = member!!, createDto = QuizCreateDto(
                description = "desc",
                answer = "answer",
                explanation = "explanation",
                examples = arrayOf("example1", "example2", "example3"),
                skillId = skill!!.id
            )
        )
        val quizUpdateDto = QuizUpdateDto(
            description = "edited description",
            answer = "edited answer",
            explanation = "edited explanation",
            examples = arrayOf("ex1", "ex2", "ex3"),
        )

        // Expected
        this.mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/quiz/{quizId}", quiz.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(quizUpdateDto))
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "quiz-update",
                    pathParameters(
                        parameterWithName("quizId").description("Id of the quiz")
                    ),
                    requestFields(
                        fieldWithPath("description").description("Quiz description")
                            .attributes(
                                Attributes.key("constraint").value("Description must be between 10 and 5000 characters")
                            ),
                        fieldWithPath("answer").description("Quiz answer"),
                        fieldWithPath("explanation").description("Quiz explanation"),
                        fieldWithPath("examples").description("Quiz examples").optional(),
                    ),
                    responseFields(
                        fieldWithPath("body.id").description("Quiz id"),
                        fieldWithPath("body.description").description("Quiz description"),
                        fieldWithPath("body.answer").description("Quiz answer"),
                        fieldWithPath("body.explanation").description("Quiz explanation"),
                        fieldWithPath("body.examples").description("Quiz examples"),
                        fieldWithPath("body.skill").description("Skill field of the Quiz"),
                        fieldWithPath("body.submitterName").description("Quiz Submitter"),
                        fieldWithPath("error").description("Error Response"),
                    )
                )
            )
    }

    @Test
    @DisplayName("Delete Quiz")
    fun deleteQuizTest() {
        // Given
        val quiz = quizService.createQuiz(
            submitter = member!!, createDto = QuizCreateDto(
                description = "desc",
                answer = "answer",
                explanation = "explanation",
                examples = arrayOf("example1", "example2", "example3"),
                skillId = skill!!.id
            )
        )

        // Expected
        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/quiz/{quizId}", quiz.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "quiz-delete",
                    pathParameters(
                        parameterWithName("quizId").description("Id of the quiz")
                    ),
                    responseFields(
                        fieldWithPath("body").description("Quiz id"),
                        fieldWithPath("error").description("Error Response"),
                    )
                )
            )
    }

}
