package kr.quidev.quiz.service

import kr.quidev.quiz.domain.entity.Example
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.repository.ExampleRepository
import kr.quidev.quiz.repository.QuizRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class QuizService(
    val quizRepository: QuizRepository,
    val exampleRepository: ExampleRepository,
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
        val examples = mutableListOf<Example>()
        for (example in arr) {
            examples.add(createExample(example, quiz))
        }
        quiz.example = examples
        log.info("created new quiz:{}", quiz)

        return quiz
    }

    fun createExample(text: String, quiz: Quiz): Example {
        val example = Example(text = text, quiz = quiz)
        return exampleRepository.save(example);
    }

}
