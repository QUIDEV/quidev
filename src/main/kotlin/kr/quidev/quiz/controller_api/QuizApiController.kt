package kr.quidev.quiz.controller_api

import kr.quidev.common.ApiResponse
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.dto.QuizDto
import kr.quidev.quiz.service.QuizService
import kr.quidev.security.domain.MemberContext
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/quiz")
class QuizApiController(
    val quizService: QuizService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/{id}")
    fun findQuiz(@PathVariable id: Long): ApiResponse {
        val quiz = quizService.findById(id)
        return ApiResponse.ok(QuizDto.of(quiz))
    }

    @GetMapping
    fun findAll(): ApiResponse {
        return ApiResponse.ok(quizService.findAll().map { quiz -> QuizDto.of(quiz) })
    }

    @PostMapping("new")
    fun createQuiz(
        @RequestBody @Valid createDto: QuizCreateDto,
        @AuthenticationPrincipal memberContext: MemberContext,
    ): ApiResponse {
        val quiz = quizService.createQuiz(memberContext.member, createDto)
        return ApiResponse.ok(mapOf(Pair("id", quiz.id)))
    }

}
