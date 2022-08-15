package kr.quidev.quiz.domain.entity

import javax.persistence.*

@Entity
class Quiz(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 5000)
    var description: String,
    var answer: String,

    @OneToMany(mappedBy = "quiz")
    var example: List<Example> = mutableListOf(),

    ) {
    override fun toString(): String {
        return "Quiz(id=$id, description='$description', answer='$answer', example=$example)"
    }
}
