package kr.quidev.quiz.domain.dto

data class QuizSearch(
    var page: Int = 0,
    var pageSize: Int = 10,
) {
    fun getOffset(): Long {
        return (page * pageSize).toLong()
    }
}
