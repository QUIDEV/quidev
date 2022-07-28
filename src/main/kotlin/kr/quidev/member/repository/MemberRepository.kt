package kr.quidev.member.repository

import kr.quidev.member.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findMemberByEmail(email: String): Optional<Member>
}
