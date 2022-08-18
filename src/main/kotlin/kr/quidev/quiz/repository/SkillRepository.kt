package kr.quidev.quiz.repository

import kr.quidev.quiz.domain.entity.Skill
import org.springframework.data.jpa.repository.JpaRepository

interface SkillRepository : JpaRepository<Skill, Long>
