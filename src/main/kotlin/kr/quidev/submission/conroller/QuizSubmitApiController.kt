package kr.quidev.submission.conroller

import kr.quidev.quiz.domain.response.QuizSubmitResponse
import kr.quidev.security.domain.MemberContext
import kr.quidev.submission.service.SubmissionService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/submit")
class QuizSubmitApiController(
    val submissionService: SubmissionService
) {
    @PostMapping("/{quizId}")
    fun submitAnswer(
        @PathVariable quizId: Long,
        answer: String,
        @AuthenticationPrincipal memberContext: MemberContext,
    ): QuizSubmitResponse {
        return submissionService.submit(memberContext.member, quizId, answer)
    }

}
