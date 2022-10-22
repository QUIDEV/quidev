package kr.quidev.quiz.repository

import kr.quidev.member.domain.entity.Member
import kr.quidev.quiz.domain.entity.Quiz
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface QuizRepository : JpaRepository<Quiz, Long>, QuizRepositoryCustom {

    fun findAllBySubmitter(pageable: Pageable, member: Member): Page<Quiz>
}
