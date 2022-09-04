package kr.quidev.submission.service

import kr.quidev.member.domain.entity.Member
import kr.quidev.quiz.domain.response.QuizResponse
import kr.quidev.quiz.repository.QuizRepository
import kr.quidev.submission.dao.SubmissionRepository
import kr.quidev.submission.domain.Submission
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SubmissionService(
    val quizRepository: QuizRepository,
    val submissionRepository: SubmissionRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun submit(member: Member, quizId: Long, answer: String): QuizResponse {
        val quiz = quizRepository.findById(quizId).orElseThrow()
        val result = quiz.answer == answer
        if (result) {
            log.info("{} solved {}", member.name, quiz.id)
        }

        val submission = Submission(quiz, member, result, answer)
        submissionRepository.save(submission)

        return QuizResponse.of(submission.id!!, result, quiz.explanation)
    }

    fun findById(id: Long): Submission {
        return submissionRepository.findById(id).orElseThrow()
    }

}
