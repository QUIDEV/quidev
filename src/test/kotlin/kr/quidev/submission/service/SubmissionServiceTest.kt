package kr.quidev.submission.service

import kr.quidev.member.domain.dto.MemberDto
import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.quiz.domain.dto.QuizCreateDto
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.domain.enums.ProgrammingLanguage
import kr.quidev.quiz.service.QuizService
import kr.quidev.quiz.service.SkillService
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
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
internal class SubmissionServiceTest {

    @Autowired
    private lateinit var submissionService: SubmissionService

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var quizService: QuizService

    @Autowired
    private lateinit var skillService: SkillService

    private val log = LoggerFactory.getLogger(javaClass)

    @ParameterizedTest
    @CsvSource(
        "'shane0', '1234', 'shane@quidev.kr",
    )
    fun submit(@AggregateWith(MemberAggregator::class) member: MemberDto) {
        // Given
        val member = memberService.createMember(member)
        val java = skillService.save(Skill(name = ProgrammingLanguage.JAVA.getValue()))
        val correctAnswer = "1234"
        val wrongAnswer = "1235"
        val quiz = quizService.createQuiz(
            submitter = member, createDto = QuizCreateDto(
                description = "desc",
                answer = correctAnswer,
                skillId = java.id,
                explanation = "explanation",
                examples = arrayOf(
                    "ex1",
                    "ex2",
                    "ex3"
                )
            )
        )

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
            return MemberDto(name = name, email = email, password = pass)
        }
    }

}
