package kr.quidev.member.service

import kr.quidev.member.domain.Member

interface MemberService {
    fun createMember(member: Member)
}
