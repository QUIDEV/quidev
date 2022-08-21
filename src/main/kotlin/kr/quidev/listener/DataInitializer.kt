package kr.quidev.listener

import kr.quidev.member.domain.entity.Member
import kr.quidev.member.repository.MemberRepository
import kr.quidev.quiz.domain.entity.Quiz
import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.repository.SkillRepository
import kr.quidev.quiz.service.QuizService
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataInitializer(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val quizService: QuizService,
    private val skillRepository: SkillRepository,
) : ApplicationListener<ContextRefreshedEvent?> {
    private var alreadySetup = false

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (alreadySetup) {
            return
        }
        setupSecurityResources()
        alreadySetup = true
    }

    private fun setupSecurityResources() {
        createDefaultUser("shane", "1234", "shane")
        createDefaultQuiz()
    }

    private fun createDefaultQuiz() {
        val desc =
            "Given the string \"helloworld\" saved in a variable called str, what would str.substring(2, 5) return?"
        val answer = "llo"

        val skill = Skill(name = "java")
        skillRepository.save(skill)
        val quiz = Quiz(description = desc, answer = answer, skill = skill)
        quizService.createQuiz(quiz, arrayOf("hello", "ell", "low", "world", "wo"))
    }

    @Transactional
    fun createDefaultUser(
        userName: String,
        password: String,
        email: String,
    ): Member {
        var member = memberRepository.findMemberByEmail(userName).orElse(null)
        if (member == null) {
            member = Member(name = userName, password = passwordEncoder.encode(password), email = email)
        }
        return memberRepository.save(member)
    }

}
