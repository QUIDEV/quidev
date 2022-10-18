package kr.quidev.member.controller

import kr.quidev.member.domain.dto.MemberDto
import kr.quidev.member.service.MemberService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

@Controller
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping("/join")
    fun createMember(memberDto: MemberDto): String {
        memberService.createMember(memberDto)
        return "redirect:/"
    }

}
