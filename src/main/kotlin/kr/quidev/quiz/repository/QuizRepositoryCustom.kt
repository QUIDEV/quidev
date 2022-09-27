package kr.quidev.quiz.repository

import kr.quidev.quiz.domain.dto.QuizSearch
import kr.quidev.quiz.domain.entity.Quiz

interface QuizRepositoryCustom {
    fun getList(pageSize: Long, page: Long): List<Quiz>
    fun searchQuiz(quizSearch: QuizSearch): List<Quiz>
}
