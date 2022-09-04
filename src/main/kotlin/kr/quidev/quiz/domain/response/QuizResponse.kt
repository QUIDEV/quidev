package kr.quidev.quiz.domain.response

class QuizResponse(
    val submissionId: Long,
    val result: Boolean,
    val explanation: String,
) {

    companion object {
        fun of(submissionId: Long, result: Boolean, explanation: String): QuizResponse {
            return QuizResponse(submissionId, result, explanation)
        }
    }
}
