package kr.quidev.member.service

import kr.quidev.member.domain.Member
import kr.quidev.member.domain.MemberDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class MemberServiceTest {

    @Autowired
    private lateinit var memberService: MemberService

    fun addMember(name: String): Member {
        val memberDto = MemberDto(id = null, name = name, password = "1234", email = "shane@quidev.kr");
        val member = Member.fromDto(memberDto)
        return memberService.createMember(member)
    }

    @ParameterizedTest
    @ValueSource(strings = ["shane", "jenny", "coupang"])
    fun createMember(name: String) {
        val member = addMember(name)
        val findById = memberService.findById(member.id!!)
        assertThat(member).isEqualTo(findById.orElseThrow())
    }

    @ParameterizedTest
    @ValueSource(strings = ["shane", "jenny", "coupang"])
    fun findAll(name: String) {
        val beforeSize = memberService.findAll().size
        addMember(name)
        assertThat(memberService.findAll()).hasSize(beforeSize + 1)
    }

}
