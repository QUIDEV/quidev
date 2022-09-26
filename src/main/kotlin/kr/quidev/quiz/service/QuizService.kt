package kr.quidev.quiz.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.dto.QuizEditDto
import kr.quidev.quiz.domain.dto.QuizSearch
import kr.quidev.quiz.domain.entity.Example
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.repository.ExampleRepository
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
    val exampleRepository: ExampleRepository,
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
        var skill = skillRepository.findById(createDto.skillId!!).orElseThrow()
        val quiz =
            Quiz(
                description = createDto.description!!,
                answer = createDto.answer!!,
                explanation = createDto.explanation!!,
                skill = skill,
                submitter = submitter
            )
        quizRepository.save(quiz)
        for (example in createDto.examples) {
            quiz.examples.add(createExample(example, quiz))
        }
        log.info("created new quiz:{}", quiz)

        return quiz
    }

    fun searchQuiz(quizSearch: QuizSearch): List<Quiz> {
        return quizRepository.searchQuiz(quizSearch)
    }

    fun edit(memberContext: MemberContext, id: Long, edit: QuizEditDto): Quiz {
        val original = quizRepository.findById(id).orElseThrow()

        if (original.submitter.id != memberContext.member.id) {
            throw IllegalAccessException("not allowed to edit")
        }

        original.description = edit.description!!
        original.answer = edit.answer!!
        original.explanation = edit.explanation!!
        original.examples.clear()
        for (example in edit.examples) {
            original.examples.add(createExample(example, original))
        }
        return original
    }

    private fun createExample(text: String, quiz: Quiz): Example {
        val example = Example(text = text, quiz = quiz)
        return exampleRepository.save(example)
    }

    fun deleteQuiz(memberContext: MemberContext, id: Long) {
        val quiz = quizRepository.findById(id).orElseThrow()
        if(quiz.submitter.id != memberContext.member.id) {
            throw IllegalAccessException("not allowed to delete")
        }
        quizRepository.delete(quiz)
    }

}
