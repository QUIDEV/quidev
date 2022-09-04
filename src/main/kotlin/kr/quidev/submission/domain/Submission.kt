package kr.quidev.submission.domain

import kr.quidev.member.domain.entity.Member
import kr.quidev.quiz.domain.entity.Quiz
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Submission(

    @ManyToOne(fetch = FetchType.LAZY)
    var quiz: Quiz,
    @ManyToOne(fetch = FetchType.LAZY)
    var member: Member,

    var result: Boolean,
    var answer: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreationTimestamp
    var createdDate: LocalDateTime? = null

}
