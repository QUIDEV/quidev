package kr.quidev.member.service

import kr.quidev.member.domain.entity.Member

interface MemberService {

    fun createMember(member: Member): Member
    fun findAll(): MutableList<Member>
    fun findById(id: Long): Member?
    fun findByEmail(email: String): Member?
}
