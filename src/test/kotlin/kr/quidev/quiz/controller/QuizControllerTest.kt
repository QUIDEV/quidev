package kr.quidev.quiz.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import javax.transaction.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
internal class QuizControllerTest {

    @Autowired
    private lateinit var controller: QuizController

    @Test
    fun createQuiz() {
        val quiz = controller.createQuiz("desc", "answer", arrayOf("candi1", "candi2", "candi3"))
        val findQuiz = controller.findQuiz(quiz.id!!)
        assertThat(findQuiz.example).hasSize(3)
    }

}