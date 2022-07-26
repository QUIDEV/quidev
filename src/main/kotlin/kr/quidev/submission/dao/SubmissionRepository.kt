package kr.quidev.submission.dao

import kr.quidev.submission.domain.Submission
import org.springframework.data.jpa.repository.JpaRepository

interface SubmissionRepository : JpaRepository<Submission, Long> {
}
