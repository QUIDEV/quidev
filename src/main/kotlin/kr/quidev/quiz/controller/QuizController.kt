package kr.quidev.quiz.controller

import kr.quidev.quiz.service.SkillService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class QuizController(
    val skillService: SkillService
) {

    @GetMapping("/quiz/add")
    fun addQuiz(
        @PageableDefault page: Pageable,
        model: Model
    ): String {
        val skills = skillService.findAll(page)
        model.addAttribute("skills", skills)
        return "quiz/add/quiz-add"
    }

}
