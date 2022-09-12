package kr.quidev.quiz.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.quiz.domain.entity.Example
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.domain.entity.QuizCreateDto
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.repository.ExampleRepository
import kr.quidev.quiz.repository.QuizRepository
import kr.quidev.quiz.repository.SkillRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class QuizService(
    val quizRepository: QuizRepository,
    val exampleRepository: ExampleRepository,
    val skillRepository: SkillRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun findById(id: Long): Optional<Quiz> {
        return quizRepository.findById(id)
    }

    fun findAll(): MutableList<Quiz> {
        return quizRepository.findAll()
    }

    fun createQuiz(quiz: Quiz, arr: Array<String>): Quiz {
        val quiz = quizRepository.save(quiz)
        for (example in arr) {
            quiz.examples.add(createExample(example, quiz))
        }
        log.info("created new quiz:{}", quiz)

        return quiz
    }

    fun createQuiz(submitter: Member, createDto: QuizCreateDto): Quiz {
        var skill: Skill? = null
        if (createDto.skillId != null) {
            skill = skillRepository.findById(createDto.skillId).orElseThrow()
        }
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

    fun createExample(text: String, quiz: Quiz): Example {
        val example = Example(text = text, quiz = quiz)
        return exampleRepository.save(example);
    }

}
