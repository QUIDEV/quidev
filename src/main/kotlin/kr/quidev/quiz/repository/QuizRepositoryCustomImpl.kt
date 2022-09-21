package kr.quidev.quiz.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.quidev.quiz.domain.dto.QuizSearch
import kr.quidev.quiz.domain.entity.QQuiz.quiz
import kr.quidev.quiz.domain.entity.Quiz
import org.springframework.stereotype.Repository

@Repository
class QuizRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : QuizRepositoryCustom {

    override fun getList(pageSize: Long, page: Long): List<Quiz> {
        return jpaQueryFactory.selectFrom(quiz)
            .orderBy(quiz.id.desc())
            .limit(pageSize)
            .offset(page * pageSize)
            .fetch()
    }

    override fun searchQuiz(quizSearch: QuizSearch): List<Quiz> {
        return jpaQueryFactory.selectFrom(quiz)
            .orderBy(quiz.id.desc())
            .limit(quizSearch.pageSize.toLong())
            .offset(quizSearch.getOffset())
            .fetch()
    }

}
