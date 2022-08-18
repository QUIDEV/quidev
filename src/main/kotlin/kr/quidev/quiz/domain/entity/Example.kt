package kr.quidev.quiz.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Example(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var text: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    var quiz: Quiz,

    ) {
    override fun toString(): String {
        return "Example(text='$text')"
    }
}
