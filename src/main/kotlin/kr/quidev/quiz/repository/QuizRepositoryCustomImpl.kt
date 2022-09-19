package kr.quidev.quiz.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.quidev.quiz.domain.entity.QQuiz
import kr.quidev.quiz.domain.entity.Quiz
import org.springframework.stereotype.Repository

@Repository
class QuizRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : QuizRepositoryCustom {

    override fun getList(pageSize: Long, page: Long): List<Quiz> {
        return jpaQueryFactory.selectFrom(QQuiz.quiz)
            .limit(pageSize)
            .offset(page * pageSize)
            .fetch()
    }

}
