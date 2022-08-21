package kr.quidev.quiz.controller

import kr.quidev.quiz.domain.entity.QuizDto
import kr.quidev.quiz.repository.SkillRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class SkillController(
    val skillRepository: SkillRepository
) {

    @GetMapping("/skills/{id}")
    fun playQuiz(@PathVariable id: Long, model:Model): String {
        val skill = skillRepository.findById(id).orElseThrow()
        val quizzes = skill.quizzes
        if (quizzes.isNotEmpty()) {
            val randomQuiz = QuizDto.of(quizzes.shuffled()[0])
            model.addAttribute("quiz", randomQuiz)
        }
        return "skill/random-quiz"
    }

}
