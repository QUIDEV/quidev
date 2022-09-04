package kr.quidev.submission.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.service.QuizService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import org.junit.jupiter.params.provider.CsvSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
internal class SubmissionServiceTest {

    @Autowired
    private lateinit var submissionService: SubmissionService

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var quizService: QuizService

    private val log = LoggerFactory.getLogger(javaClass)

    @ParameterizedTest
    @CsvSource(
        "'shane', '1234', 'shane@quidev.kr",
    )
    fun submit(@AggregateWith(MemberAggregator::class) member: Member) {
        // Given
        val member = memberService.createMember(member)
        val correctAnswer = "1234"
        val wrongAnswer = "1235"
        val quiz = Quiz(description = "desc", answer = correctAnswer, explanation = "...")
        quizService.createQuiz(quiz, arrayOf("candi1", "candi2", "candi3"))

        // When
        val submitResult = submissionService.submit(member, quiz.id!!, correctAnswer)

        // Then
        assertThat(submitResult.result).isTrue

        val submission = submissionService.findById(submitResult.submissionId)
        assertThat(submission).isNotNull
        assertThat(submission.result).isTrue
        assertThat(submission.member).isEqualTo(member)
        assertThat(submission.answer).isEqualTo(correctAnswer)

        val submitResult2 = submissionService.submit(member, quiz.id!!, wrongAnswer)
        assertThat(submitResult2.result).isFalse
        val submission2 = submissionService.findById(submitResult2.submissionId)
        assertThat(submission2.answer).isEqualTo(wrongAnswer)


    }


    companion object
    class MemberAggregator : ArgumentsAggregator {
        override fun aggregateArguments(accessor: ArgumentsAccessor, context: ParameterContext?): Any {
            val name = accessor.getString(0)
            val pass = accessor.getString(1)
            val email = accessor.getString(2)
            return Member(name = name, email = email, password = pass)
        }
    }

}
