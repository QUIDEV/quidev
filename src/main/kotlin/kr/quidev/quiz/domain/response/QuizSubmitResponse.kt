package kr.quidev.quiz.domain.response

class QuizSubmitResponse(
    val submissionId: Long,
    val result: Boolean,
    val explanation: String,
) {

    companion object {
        fun of(submissionId: Long, result: Boolean, explanation: String): QuizSubmitResponse {
            return QuizSubmitResponse(submissionId, result, explanation)
        }
    }
}
