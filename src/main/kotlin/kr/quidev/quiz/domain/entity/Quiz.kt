package kr.quidev.quiz.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import kr.quidev.member.domain.entity.Member
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Quiz(

    @Column(nullable = false, length = 5000)
    var description: String,

    var answer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    var skill: Skill?,
    var explanation: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitter_id")
    val submitter: Member
) {
    constructor(description: String, answer: String, explanation: String, submitter: Member) :
            this(
                description = description,
                answer = answer,
                submitter = submitter,
                skill = null,
                explanation = explanation
            )

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreationTimestamp
    var createdDate: LocalDateTime? = null

    @UpdateTimestamp
    var updatedDate: LocalDateTime? = null

    @OneToMany(mappedBy = "quiz")
    val examples = mutableListOf<Example>()

    override fun toString(): String {
        return "Quiz(id=$id, description='$description', answer='$answer', example=$examples)"
    }
}
