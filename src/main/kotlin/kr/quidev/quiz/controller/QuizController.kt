package kr.quidev.quiz.controller

import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.service.QuizService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quiz")
class QuizController(
    val quizService: QuizService
) {

    @GetMapping("/{id}")
    fun findQuiz(@PathVariable id: Long): Quiz {
        val quiz = quizService.findById(id).orElseThrow()
        // TODO user DTO
        return quiz
    }

    @GetMapping()
    fun findAll(): MutableList<Quiz> {
        return quizService.findAll()
    }

    @PostMapping
    fun createQuiz(
        desc: String, answer: String, examples: Array<String>
    ): Quiz {
        val quiz = Quiz(description = desc, answer = answer)
        quizService.createQuiz(quiz, examples)
        return quiz
    }

}
