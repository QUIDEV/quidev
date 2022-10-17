package kr.quidev.member.controller

import kr.quidev.member.domain.dto.MemberDto
import kr.quidev.member.domain.entity.Member
import kr.quidev.member.service.MemberService
import kr.quidev.security.BcryptEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

@Controller
class MemberController(
    private val passwordEncoder: BcryptEncoder,
    private val memberService: MemberService,
) {

    @PostMapping("/join")
    fun createMember(memberDto: MemberDto): String {
        val member: Member = Member.fromDto(memberDto)
        member.password = passwordEncoder.encode(member.password)
        memberService.createMember(member)
        return "redirect:/"
    }

}
