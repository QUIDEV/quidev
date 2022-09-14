package kr.quidev.quiz.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.entity.QuizCreateDto
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.domain.enums.ProgrammingLanguage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
internal class QuizServiceTest {

    @Autowired
    private lateinit var quizService: QuizService

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var skillService: SkillService

    @Test
    fun createQuizTest() {
        // Given
        val findAllSize = quizService.findAll().size
        val member = memberService.createMember(Member(name = "name", password = "", email = ""))
        val java = skillService.save(Skill(name = ProgrammingLanguage.JAVA.getValue()))

        val description = "desc"
        val answer = "something answer"

        // When
        val quiz = quizService.createQuiz(
            submitter = member, createDto = QuizCreateDto(
                description = description,
                answer = answer,
                skillId = java.id,
                explanation = "explanation",
                examples = arrayOf(
                    "ex1",
                    "ex2",
                    "ex3"
                )
            )
        )

        // Then
        val findById = quizService.findById(quiz.id!!).orElseThrow()
        assertThat(findById.description).isEqualTo(description)
        assertThat(findById.answer).isEqualTo(answer)
        assertThat(findById.examples).hasSize(3)
        assertThat(quizService.findAll().size).isEqualTo(findAllSize + 1)
    }
}
