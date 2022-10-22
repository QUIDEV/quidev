package kr.quidev.quiz.domain.dto

import kr.quidev.quiz.domain.entity.Quiz
import java.time.LocalDateTime

class QuizDto(
    val id: Long?,
    val description: String,
    val answer: String,
    val explanation: String,
    val skill: String,
    val submitterName: String,
    val examples: List<String>,
    val updatedDate: LocalDateTime?,
    val createdDate: LocalDateTime?,
) {

    companion object {
        fun of(quiz: Quiz): QuizDto {
            return QuizDto(
                id = quiz.id,
                description = quiz.description,
                answer = quiz.answer,
                explanation = quiz.explanation,
                skill = quiz.skill.name,
                submitterName = quiz.submitter.name,
                examples = quiz.examples.map { e -> e.text },
                updatedDate = quiz.updatedDate,
                createdDate = quiz.createdDate,
            )
        }
    }

}
