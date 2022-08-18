package kr.quidev.quiz.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Quiz(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 5000)
    var description: String,
    var answer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    var skill: Skill?,

    ) {
    constructor(description: String, answer: String) : this(description = description, answer = answer, skill = null)

    @OneToMany(mappedBy = "quiz")
    val examples = mutableListOf<Example>()

    override fun toString(): String {
        return "Quiz(id=$id, description='$description', answer='$answer', example=$examples)"
    }
}
