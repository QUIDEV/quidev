package kr.quidev.quiz.domain.response

class QuizResponse(
    val result: Boolean,
    val explanation: String
) {

    companion object {
        fun of(result: Boolean, explanation: String): QuizResponse {
            return QuizResponse(result, explanation)
        }
    }
}
