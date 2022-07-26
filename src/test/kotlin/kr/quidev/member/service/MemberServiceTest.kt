package kr.quidev.member.service

import kr.quidev.member.domain.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import org.junit.jupiter.params.provider.CsvSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class MemberServiceTest {

    @Autowired
    private lateinit var memberService: MemberService

    private val log = LoggerFactory.getLogger(javaClass)

    @ParameterizedTest
    @CsvSource(
        "'shane', '1234', 'shane@quidev.kr",
        "'jenny', 'pass', 'jenny@quidev.kr",
        "'tesla', '0000', 'elon@coupang.com"
    )
    fun createMember(@AggregateWith(MemberAggregator::class) member: Member) {
        memberService.createMember(member)
        val findById = memberService.findById(member.id!!)
        val findMember = findById.orElseThrow()
        log.info("findMember: {}", findMember)
        assertThat(member).isEqualTo(findMember)
    }

    companion object
    class MemberAggregator : ArgumentsAggregator {
        override fun aggregateArguments(accessor: ArgumentsAccessor, context: ParameterContext?): Any {
            val name = accessor.getString(0)
            val pass = accessor.getString(1)
            val email = accessor.getString(2)
            return Member(name = name, email = email, password = pass)
        }

    }

    @ParameterizedTest
    @CsvSource(
        "'park', '1234', 'park@quidev.kr",
        "'lee', 'pass', 'lee@quidev.kr",
        "'connie', '0000', 'connie@coupang.com"
    )
    fun findAll(@AggregateWith(MemberAggregator::class) member: Member) {
        val beforeSize = memberService.findAll().size
        memberService.createMember(member)
        assertThat(memberService.findAll()).hasSize(beforeSize + 1)
    }

}
