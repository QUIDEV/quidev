package kr.quidev.member.service

import kr.quidev.member.domain.dto.MemberDto
import kr.quidev.member.domain.entity.Member
import kr.quidev.member.repository.MemberRepository
import kr.quidev.security.BcryptEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: BcryptEncoder
) : MemberService {

    @Transactional
    override fun createMember(memberDto: MemberDto): Member {
        val member: Member = Member.fromDto(memberDto)
        member.password = passwordEncoder.encode(member.password)
        return memberRepository.save(member)
    }

    override fun findAll(): MutableList<Member> {
        return memberRepository.findAll()
    }

    override fun findById(id: Long): Member {
        return memberRepository.findById(id).orElse(null)
    }

    override fun findByEmail(email: String): Member? {
        return memberRepository.findMemberByEmail(email).orElse(null)
    }

}
