package kr.quidev.quiz.controller_api

import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.domain.entity.QuizDto
import kr.quidev.quiz.domain.response.QuizResponse
import kr.quidev.quiz.service.QuizService
import kr.quidev.security.domain.MemberContext
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quiz")
class QuizApiController(
    val quizService: QuizService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/{id}")
    fun findQuiz(@PathVariable id: Long): QuizDto {
        val quiz = quizService.findById(id).orElseThrow()
        return QuizDto.of(quiz);
    }

    @PostMapping("/submit/{id}")
    fun submitAnswer(
        @PathVariable id: Long,
        answer: String,
        @AuthenticationPrincipal memberContext: MemberContext,
    ): QuizResponse {
        val quiz = quizService.findById(id).orElseThrow()
        // TODO save answer data on login account
        val member = memberContext.member
        val result = quiz.answer == answer
        if (result) {
            log.info("{} solved {}", member.name, quiz.id)
        }
        return QuizResponse.of(result, quiz.explanation)
    }

    @GetMapping
    fun findAll(): MutableList<Quiz> {
        return quizService.findAll()
    }

    @PostMapping("new")
    fun createQuiz(
        desc: String, answer: String, explanation: String, examples: Array<String>
    ): Quiz {
        val quiz = Quiz(description = desc, answer = answer, explanation = explanation)
        quizService.createQuiz(quiz, examples)
        return quiz
    }

}
