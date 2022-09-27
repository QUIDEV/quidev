package kr.quidev.quiz.domain.dto

import kr.quidev.quiz.domain.entity.Quiz
import java.util.function.Consumer

data class PlayQuizDto(
    val id: Long?,
    val description: String,
    val skill: String?,
    val examples: List<String>,
) {
    companion object {
        fun of(quiz: Quiz): PlayQuizDto {
            val answer = mutableListOf<String>()
            answer.add(quiz.answer)
            quiz.examples.shuffled().stream().limit(3).map { e -> e.text }.forEach(Consumer { s -> answer.add(s) })
            answer.shuffle()

            return PlayQuizDto(
                id = quiz.id,
                description = quiz.description,
                skill = quiz.skill?.let { skill -> skill.name },
                examples = answer
            )
        }
    }

}
