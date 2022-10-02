package kr.quidev.quiz.repository

import kr.quidev.common.TestUtils.Companion.randomString
import kr.quidev.member.domain.entity.Member
import kr.quidev.member.repository.MemberRepository
import kr.quidev.quiz.domain.dto.QuizSearch
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.domain.entity.Skill
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class QuizRepositoryCustomImplTest {

    @Autowired
    lateinit var quizRepository: QuizRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var skillRepository: SkillRepository


    private var member: Member? = null
    private var skill: Skill? = null

    @BeforeAll
    fun beforeAll() {
        skill = skillRepository.save(Skill(null, null, "name"))
        member = memberRepository.save(Member(null, "pass", randomString(), randomString(), "role"))
    }

    @Test
    fun getList() {
        for (i in 1..35) {
            quizRepository.save(Quiz("desc$i", "answ", skill!!, "expl", member!!))
        }

        val list = quizRepository.getList(10, 1)
        assertThat(list).hasSize(10)
        assertThat(list[0].description).isEqualTo("desc25")

        val page0 = quizRepository.getList(10, 0)
        assertThat(page0[0].description).isEqualTo("desc35")
    }

    @Test
    fun searchQuizTest() {
        for (i in 1..35) {
            quizRepository.save(Quiz("desc$i", "answ", skill!!, "expl", member!!))
        }

        val list = quizRepository.searchQuiz(QuizSearch(page = 1, pageSize = 10))
        assertThat(list).hasSize(10)
        assertThat(list[0].description).isEqualTo("desc25")

        val page0 = quizRepository.searchQuiz(QuizSearch())
        assertThat(page0[0].description).isEqualTo("desc35")
    }

}
