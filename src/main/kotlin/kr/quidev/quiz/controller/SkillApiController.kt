package kr.quidev.quiz.controller

import kr.quidev.quiz.domain.dto.PlayQuizDto
import kr.quidev.quiz.repository.SkillRepository
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/skills")
class SkillApiController(
    val skillRepository: SkillRepository
) {

    @GetMapping("{id}")
    fun playQuiz(@PathVariable id: Long, model: Model): PlayQuizDto? {
        val skill = skillRepository.findById(id).orElseThrow()
        val quizzes = skill.quizzes
        if (quizzes.isEmpty()) {
            return null
        }
        return PlayQuizDto.of(quizzes.shuffled()[0])
    }

}
