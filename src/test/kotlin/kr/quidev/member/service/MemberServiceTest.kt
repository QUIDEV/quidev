package kr.quidev.member.service

import kr.quidev.member.domain.dto.MemberDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import org.junit.jupiter.params.provider.CsvSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
internal class MemberServiceTest {

    @Autowired
    private lateinit var memberService: MemberService
    private val log = LoggerFactory.getLogger(javaClass)

    companion object
    class MemberDtoAggregator : ArgumentsAggregator {
        override fun aggregateArguments(accessor: ArgumentsAccessor, context: ParameterContext?): Any {
            val name = accessor.getString(0)
            val pass = accessor.getString(1)
            val email = accessor.getString(2)
            return MemberDto(name = name, email = email, password = pass)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "'shane', '1234', 'shane@quidev.kr",
        "'jenny', 'pass', 'jenny@quidev.kr",
        "'tesla', '0000', 'elon@coupang.com"
    )
    fun createMember(@AggregateWith(MemberDtoAggregator::class) memberDto: MemberDto) {
        val member = memberService.createMember(memberDto)
        val findById = memberService.findById(member.id!!)
        val findMember = findById ?: NoSuchElementException()
        log.info("findMember: {}", findMember)
        assertThat(member).isEqualTo(findMember)
    }

    @ParameterizedTest
    @CsvSource(
        "'park', '1234', 'park@quidev.kr",
        "'lee', 'pass', 'lee@quidev.kr",
        "'connie', '0000', 'connie@coupang.com"
    )
    fun findAll(@AggregateWith(MemberDtoAggregator::class) member: MemberDto) {
        val beforeSize = memberService.findAll().size
        memberService.createMember(member)
        assertThat(memberService.findAll()).hasSize(beforeSize + 1)
    }

    @Test
    fun findByEmail() {
        val shaneEmail = "shane@argonet.co.kr"
        val shane = memberService.createMember(MemberDto(name = "shane", password = "1234", email = shaneEmail))
        memberService.createMember(MemberDto(name = "jenny", password = "1234", email = "shane@ssd.co.kr"))
        memberService.createMember(MemberDto(name = "june", password = "1256", email = "june@apple.co.kr"))
        val findMember = memberService.findByEmail(shaneEmail) ?: throw NoSuchElementException()
        assertThat(findMember).isEqualTo(shane)
        assertThrows<NoSuchElementException> {
            memberService.findByEmail("invalid-email") ?: throw NoSuchElementException()
        }
    }

}
