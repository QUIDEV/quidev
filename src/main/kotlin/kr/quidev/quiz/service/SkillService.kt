package kr.quidev.quiz.service

import kr.quidev.quiz.domain.entity.Skill
import kr.quidev.quiz.repository.SkillRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SkillService(
    val skillRepository: SkillRepository
) {
    fun findAll(pageable: Pageable): Page<Skill> {
        return skillRepository.findAll(pageable)
    }

    fun save(skill: Skill): Skill {
        return skillRepository.save(skill)
    }
}
