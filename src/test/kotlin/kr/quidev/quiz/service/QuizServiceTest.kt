package kr.quidev.quiz.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.dto.QuizEditDto
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.domain.enums.ProgrammingLanguage
import kr.quidev.quiz.repository.QuizRepository
import kr.quidev.security.domain.MemberContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
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
        val findAllSize = quizRepository.findAll().size

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
        assertThat(quizRepository.findAll().size).isEqualTo(findAllSize + 1)
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
        assertThat(quizRepository.findAll()).hasSize(1)
        val findById = quizService.findById(quiz.id!!)
        assertThat(findById).isNotNull
        assertThat(findById.id).isEqualTo(quiz.id)
        assertThat(findById.description).isEqualTo(description)
    }

    @Test
    fun findAllTest() {
        // Given
        val totalSize = 100L
        for (i in 1..totalSize) {
            quizService.createQuiz(
                submitter = member!!, createDto = QuizCreateDto(
                    description = "desc$i",
                    answer = "something answer$i",
                    skillId = java!!.id,
                    explanation = "explanation$i",
                    examples = arrayOf(
                        "ex1",
                        "ex2",
                        "ex3"
                    )
                )
            )
        }

        // When
        val findAllPageSize20 = quizService.findAll(Pageable.ofSize(20))
        val findAllPageSize10WithPage1 = quizService.findAll(Pageable.ofSize(10).withPage(1))

        // Then
        assertThat(findAllPageSize20).hasSize(20)
        assertThat(findAllPageSize20.totalElements).isEqualTo(totalSize)
        assertThat(findAllPageSize20.totalPages).isEqualTo((totalSize - 1) / 20 + 1)

        assertThat(findAllPageSize10WithPage1).hasSize(10)
        assertThat(findAllPageSize10WithPage1.content[0].description).isEqualTo("desc11")
    }

    @Test
    fun editQuizTest() {
        // Given
        val quiz = quizService.createQuiz(
            submitter = member!!, createDto = QuizCreateDto(
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
        )

        val updatedDescription = "changed desc"
        val updatedAnswer = "changed answer"
        val updatedExplanation = "updated explanation"
        val examples = arrayOf("edited ex1", "edited ex2", "edited ex3")
        val quizEditDto = QuizEditDto(
            description = updatedDescription,
            answer = updatedAnswer,
            explanation = updatedExplanation,
            examples = examples
        )

        // When
        val memberContext = Mockito.mock(MemberContext::class.java)
        Mockito.`when`(memberContext.member).thenReturn(member)

        quizService.edit(memberContext, quiz.id!!, quizEditDto)

        // Then
        val findById = quizService.findById(quiz.id!!)
        assertThat(findById.description).isEqualTo(updatedDescription)
        assertThat(findById.answer).isEqualTo(updatedAnswer)
        assertThat(findById.explanation).isEqualTo(updatedExplanation)
        assertThat(findById.examples).hasSize(examples.size)
        for (i in examples.indices) {
            assertThat(findById.examples[i].text).isEqualTo(examples[i])
        }

    }

    @Test
    @DisplayName("Delete quiz")
    fun test() {
        // Given
        val quiz = quizService.createQuiz(
            submitter = member!!, createDto = QuizCreateDto(
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
        )

        // When
        val memberContext = Mockito.mock(MemberContext::class.java)
        Mockito.`when`(memberContext.member).thenReturn(member)

        quizService.deleteQuiz(memberContext = memberContext, id = quiz.id!!)

        // Then
        assertThat(quizRepository.findById(quiz.id!!)).isEmpty
        assertThat(quizRepository.findAll()).isEmpty()

    }


}
