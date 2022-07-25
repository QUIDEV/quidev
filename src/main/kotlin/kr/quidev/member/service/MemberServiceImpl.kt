package kr.quidev.member.service

import kr.quidev.member.domain.Member
import kr.quidev.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository
) : MemberService {

    @Transactional
    override fun createMember(member: Member): Member {
        return memberRepository.save(member)
    }

    override fun findAll(): MutableList<Member> {
        return memberRepository.findAll()
    }

    override fun findById(id: Long): Optional<Member> {
        return memberRepository.findById(id)
    }

}
