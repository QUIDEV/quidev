package kr.quidev.quiz.repository

import kr.quidev.quiz.domain.entity.Quiz
import org.springframework.data.jpa.repository.JpaRepository

interface QuizRepository : JpaRepository<Quiz, Long> {
}
