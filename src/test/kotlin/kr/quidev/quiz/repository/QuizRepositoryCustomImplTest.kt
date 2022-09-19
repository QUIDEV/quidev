package kr.quidev.quiz.repository

import kr.quidev.member.domain.entity.Member
import kr.quidev.member.repository.MemberRepository
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.domain.entity.Skill
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
internal class QuizRepositoryCustomImplTest {

    @Autowired
    lateinit var quizRepository: QuizRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var skillRepository: SkillRepository


    @Test
    fun getList() {

        val member = Member(null, "pass", "name", "em", "role")
        memberRepository.save(member)

        val skill = Skill(null, null, "name")
        skillRepository.save(skill)

        for (i in 1..35) {
            quizRepository.save(Quiz("desc$i", "answ", skill, "expl", member))
        }

        val list = quizRepository.getList(10, 1)
        assertThat(list).hasSize(10)
        assertThat(list[0].description).isEqualTo("desc11")
    }

}
