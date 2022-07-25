package kr.quidev.member.domain

import javax.persistence.*

@Entity
class Member(
    name: String,
    password: String,
    email: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false
        if (password != other.password) return false
        if (name != other.name) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + password.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }


}
