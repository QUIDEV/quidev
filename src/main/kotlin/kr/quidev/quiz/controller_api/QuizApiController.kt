package kr.quidev.quiz.controller_api

import kr.quidev.common.dto.ApiResponse
import kr.quidev.common.dto.PageableContent
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.dto.QuizDto
import kr.quidev.quiz.domain.dto.QuizEditDto
import kr.quidev.quiz.domain.dto.QuizSearch
import kr.quidev.quiz.service.QuizService
import kr.quidev.security.domain.MemberContext
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/quiz")
class QuizApiController(
    val quizService: QuizService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createQuiz(
        @RequestBody @Valid createDto: QuizCreateDto,
        @AuthenticationPrincipal memberContext: MemberContext,
    ): ApiResponse {
        val quiz = quizService.createQuiz(memberContext.member, createDto)
        return ApiResponse.ok(mapOf(Pair("id", quiz.id)))
    }

    @GetMapping("/{id}")
    fun findQuiz(@PathVariable id: Long): ApiResponse {
        val quiz = quizService.findById(id)
        return ApiResponse.ok(QuizDto.of(quiz))
    }

    @GetMapping
    fun findAll(
        @SortDefault.SortDefaults(
            SortDefault(sort = ["id"], direction = Sort.Direction.ASC),
            SortDefault(sort = ["createdDate"], direction = Sort.Direction.DESC)
        )
        @PageableDefault pageable: Pageable
    ): ApiResponse {
        val pagedContent = quizService.findAll(pageable).map { quiz -> QuizDto.of(quiz) }
        return ApiResponse.ok(pagedContent)
    }

    @GetMapping("search")
    fun searchQuiz(@ModelAttribute quizSearch: QuizSearch): ApiResponse {
        return ApiResponse.ok(
            quizService.searchQuiz(quizSearch)
        )
    }

    @PatchMapping("/{id}")
    fun editQuiz(
        @PathVariable id: Long,
        @RequestBody @Valid editDto: QuizEditDto,
        @AuthenticationPrincipal memberContext: MemberContext,
    ): ApiResponse {
        val quiz = quizService.edit(memberContext = memberContext, id = id, edit = editDto)
        return ApiResponse.ok(QuizDto.of(quiz))
    }

    @DeleteMapping("/{id}")
    fun deleteQuiz(
        @PathVariable id: Long,
        @AuthenticationPrincipal memberContext: MemberContext,
    ): ApiResponse {
        quizService.deleteQuiz(memberContext = memberContext, id = id)
        return ApiResponse.ok(true)
    }

}
