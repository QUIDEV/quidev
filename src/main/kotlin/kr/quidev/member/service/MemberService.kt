package kr.quidev.member.service

import kr.quidev.member.domain.Member
import java.util.*

interface MemberService {

    fun createMember(member: Member): Member
    fun findAll(): MutableList<Member>
    fun findById(id: Long): Optional<Member>
}
