package kr.quidev.quiz.domain.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class QuizEditDto(

    @field:NotBlank
    @field:Length(max = 5000)
    val description: String?,

    @field:NotBlank
    @field:Length(max = 5000)
    val answer: String?,

    @field:NotBlank
    @field:Length(max = 5000)
    val explanation: String?,

    val examples: Array<String>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuizEditDto) return false

        if (description != other.description) return false
        if (answer != other.answer) return false
        if (explanation != other.explanation) return false
        if (!examples.contentEquals(other.examples)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description?.hashCode() ?: 0
        result = 31 * result + (answer?.hashCode() ?: 0)
        result = 31 * result + (explanation?.hashCode() ?: 0)
        result = 31 * result + examples.contentHashCode()
        return result
    }
}
