package kr.quidev.quiz.domain.entity

import javax.persistence.*

@Entity
class Skill(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    var parent: Skill? = null,

    var name : String

) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "skill")
    val quizzes = mutableListOf<Quiz>()

    override fun toString(): String {
        return "Skill(id=$id, parent=$parent, name='$name')"
    }

}
