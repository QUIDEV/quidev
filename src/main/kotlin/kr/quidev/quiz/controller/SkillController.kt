package kr.quidev.quiz.controller

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
    fun playQuiz(@PathVariable id: Long, model: Model): String {
        model.addAttribute("id", id)
        return "skill/random-quiz"
    }

}
