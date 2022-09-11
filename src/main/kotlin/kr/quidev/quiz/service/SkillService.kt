package kr.quidev.quiz.service

import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.repository.SkillRepository
import org.springframework.stereotype.Service

@Service
class SkillService(
    val skillRepository: SkillRepository
) {
    fun save(skill: Skill): Skill {
        return skillRepository.save(skill)
    }
}
