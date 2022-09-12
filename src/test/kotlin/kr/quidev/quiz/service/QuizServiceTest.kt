package kr.quidev.quiz.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.entity.Quiz
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

    @Test
    fun createTest() {
        val findAllSize = quizService.findAll().size
        val member = memberService.createMember(Member(name = "name", password = "", email = ""))

        val quiz = Quiz(description = "desc", answer = "1234", explanation = "...", submitter = member)
        quizService.createQuiz(quiz, arrayOf("candi1", "candi2", "candi3"))

        val findById = quizService.findById(quiz.id!!).orElseThrow()
        assertThat(findById.description).isEqualTo("desc")
        assertThat(findById.examples).hasSize(3)
        assertThat(quizService.findAll().size).isEqualTo(findAllSize + 1)
    }
}
