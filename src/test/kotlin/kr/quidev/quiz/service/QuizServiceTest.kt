package kr.quidev.quiz.service

import kr.quidev.quiz.domain.entity.Example
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

    @Test
    fun createTest() {
        val findAll = quizService.findAll()
        assertThat(findAll).isEmpty()

        val quiz = Quiz(description = "desc", answer = "1234")
        quizService.createQuiz(quiz, arrayOf("candi1", "candi2", "candi3"))

        val findById = quizService.findById(quiz.id!!).orElseThrow()
        assertThat(findById.description).isEqualTo("desc")
        assertThat(findById.example).hasSize(3)
    }
}
