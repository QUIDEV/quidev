package kr.quidev.member.entity

import javax.persistence.*

@Entity
class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    var name: String = ""

}
