package kr.quidev.quiz.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.dto.QuizSearch
import kr.quidev.quiz.domain.entity.Example
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.repository.ExampleRepository
import kr.quidev.quiz.repository.QuizRepository
import kr.quidev.quiz.repository.SkillRepository
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

    private fun createExample(text: String, quiz: Quiz): Example {
        val example = Example(text = text, quiz = quiz)
        return exampleRepository.save(example)
    }

    fun searchQuiz(quizSearch: QuizSearch): List<Quiz> {
        return quizRepository.searchQuiz(quizSearch)
    }

}
