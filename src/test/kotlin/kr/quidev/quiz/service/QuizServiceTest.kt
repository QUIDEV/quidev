package kr.quidev.quiz.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.domain.enums.ProgrammingLanguage
import kr.quidev.quiz.repository.QuizRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class QuizServiceTest {

    @Autowired
    private lateinit var quizService: QuizService

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var skillService: SkillService

    @Autowired
    private lateinit var quizRepository: QuizRepository

    private var member: Member? = null
    private var java: Skill? = null

    @BeforeAll
    fun beforeAll() {
        member = memberService.createMember(Member(name = "name", password = "", email = ""))
        java = skillService.save(Skill(name = ProgrammingLanguage.JAVA.getValue()))
    }

    @BeforeEach
    fun beforeEach() {
        quizRepository.deleteAll()
    }

    @Test
    fun createQuizTest() {
        // Given
        val findAllSize = quizService.findAll().size

        val description = "desc"
        val answer = "something answer"

        // When
        val quiz = quizService.createQuiz(
            submitter = member!!, createDto = QuizCreateDto(
                description = description,
                answer = answer,
                skillId = java!!.id,
                explanation = "explanation",
                examples = arrayOf(
                    "ex1",
                    "ex2",
                    "ex3"
                )
            )
        )

        // Then
        val findById = quizService.findById(quiz.id!!)
        assertThat(findById.description).isEqualTo(description)
        assertThat(findById.answer).isEqualTo(answer)
        assertThat(findById.examples).hasSize(3)
        assertThat(quizService.findAll().size).isEqualTo(findAllSize + 1)
    }

    @Test
    fun findByIdTest() {
        // Given
        val description = "desc"
        val createDto = QuizCreateDto(
            description = description,
            answer = "something answer",
            skillId = java!!.id,
            explanation = "explanation",
            examples = arrayOf(
                "ex1",
                "ex2",
                "ex3"
            )
        )

        // When
        val quiz = quizService.createQuiz(
            submitter = member!!, createDto = createDto
        )

        // Then
        assertThat(quizService.findAll()).hasSize(1)
        val findById = quizService.findById(quiz.id!!)
        assertThat(findById).isNotNull
        assertThat(findById.id).isEqualTo(quiz.id)
        assertThat(findById.description).isEqualTo(description)
    }

    @Test
    fun findAllTest() {
        // Given
        val quiz1Dto = QuizCreateDto(
            description = "desc",
            answer = "something answer",
            skillId = java!!.id,
            explanation = "explanation",
            examples = arrayOf(
                "ex1",
                "ex2",
                "ex3"
            )
        )
        val quiz2Dto = QuizCreateDto(
            description = "desc",
            answer = "something answer",
            skillId = java!!.id,
            explanation = "explanation",
            examples = arrayOf(
                "ex1",
                "ex2",
                "ex3"
            )
        )
        val quiz3Dto = QuizCreateDto(
            description = "desc",
            answer = "something answer",
            skillId = java!!.id,
            explanation = "explanation",
            examples = arrayOf(
                "ex1",
                "ex2",
                "ex3"
            )
        )
        val quiz1 = quizService.createQuiz(submitter = member!!, createDto = quiz1Dto)
        val quiz2 = quizService.createQuiz(submitter = member!!, createDto = quiz2Dto)
        val quiz3 = quizService.createQuiz(submitter = member!!, createDto = quiz3Dto)

        // When
        val all = quizService.findAll()

        // Then
        assertThat(all).hasSize(3)
        assertThat(all).containsExactlyInAnyOrder(quiz1, quiz2, quiz3)
    }

}
