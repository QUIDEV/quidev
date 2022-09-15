package kr.quidev.quiz.domain.entity

import kr.quidev.quiz.domain.dto.QuizCreateDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator

internal class QuizCreateDtoTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun descriptionBlank() {
        val quizCreateDto = QuizCreateDto(
            description = "",
            answer = "answer",
            explanation = "explanation",
            examples = arrayOf("e1", "e2", "e3"),
            skillId = 1L
        )
        val validate = validator.validate(quizCreateDto)
        assertThat(validate).hasSize(1)
    }

}
