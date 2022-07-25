package kr.quidev.member.service

import kr.quidev.member.domain.Member
import kr.quidev.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository
) : MemberService {

    @Transactional
    override fun createMember(member: Member) {
        memberRepository.save(member)
    }

}
