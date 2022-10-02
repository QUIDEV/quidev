package kr.quidev.quiz.service

import kr.quidev.common.exception.NotAuthorized
import kr.quidev.member.domain.entity.Member
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.dto.QuizUpdateDto
import kr.quidev.quiz.domain.dto.QuizSearch
import kr.quidev.quiz.domain.entity.Example
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.repository.QuizRepository
import kr.quidev.quiz.repository.SkillRepository
import kr.quidev.security.domain.MemberContext
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class QuizService(
    val quizRepository: QuizRepository,
    val skillRepository: SkillRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun findById(id: Long): Quiz {
        return quizRepository.findById(id).orElseThrow()
    }

    fun findAll(pageable: Pageable): Page<Quiz> {
        return quizRepository.findAll(pageable)
    }

    fun createQuiz(submitter: Member, createDto: QuizCreateDto): Quiz {
        val skill = skillRepository.findById(createDto.skillId!!).orElseThrow()
        val quiz =
            Quiz(
                description = createDto.description!!,
                answer = createDto.answer!!,
                explanation = createDto.explanation!!,
                skill = skill,
                submitter = submitter
            )
        for (example in createDto.examples) {
            quiz.examples.add(Example(text = example, quiz = quiz))
        }
        quiz.validate()
        quizRepository.save(quiz)
        log.info("created new quiz:{}", quiz)

        return quiz
    }

    fun searchQuiz(quizSearch: QuizSearch): List<Quiz> {
        return quizRepository.searchQuiz(quizSearch)
    }

    fun edit(memberContext: MemberContext, id: Long, edit: QuizUpdateDto): Quiz {
        val quiz = quizRepository.findById(id).orElseThrow()

        if (quiz.submitter.id != memberContext.member.id) {
            throw NotAuthorized()
        }

        quiz.description = edit.description!!
        quiz.answer = edit.answer!!
        quiz.explanation = edit.explanation!!
        quiz.examples.clear()
        for (example in edit.examples) {
            quiz.examples.add(Example(text = example, quiz = quiz))
        }
        quiz.validate()
        return quiz
    }

    fun deleteQuiz(memberContext: MemberContext, id: Long) {
        val quiz = quizRepository.findById(id).orElseThrow()
        if (quiz.submitter.id != memberContext.member.id) {
            throw NotAuthorized()
        }
        quizRepository.delete(quiz)
    }

}
