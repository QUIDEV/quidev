package kr.quidev.member.domain

import javax.persistence.*

@Entity
class Member(
    name: String,
    password: String,
    email: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null
    var password: String = password
    var name: String = name
    var email: String = email

    companion object {
        fun fromDto(dto: MemberDto): Member {
            return Member(name = dto.name, password = dto.password, email = dto.email);
        }
    }

}
