package kr.quidev.quiz.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import kr.quidev.common.exception.ValidationException
import kr.quidev.member.domain.entity.Member
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Quiz(
    @Column(nullable = false, length = 5000)
    var description: String,

    @Column(nullable = false, length = 5000)
    var answer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    var skill: Skill,
    var explanation: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitter_id")
    val submitter: Member
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreationTimestamp
    var createdDate: LocalDateTime? = null

    @UpdateTimestamp
    var updatedDate: LocalDateTime? = null

    @OneToMany(mappedBy = "quiz", orphanRemoval = true, cascade = [CascadeType.PERSIST])
    val examples = mutableListOf<Example>()

    override fun toString(): String {
        return "Quiz(id=$id, description='$description', answer='$answer', example=$examples)"
    }

    fun validate() {
        if (examples.size > 10) {
            throw ValidationException("examples", "too many examples")
        }
    }
}
