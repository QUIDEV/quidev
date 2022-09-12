package kr.quidev.quiz.repository

import kr.quidev.quiz.domain.entity.Skill
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SkillRepository : JpaRepository<Skill, Long> {

    fun findByName(name: String): Optional<Skill>

    @EntityGraph(attributePaths = ["quizzes"])
    override fun findAll(pageable: Pageable): Page<Skill>

}
