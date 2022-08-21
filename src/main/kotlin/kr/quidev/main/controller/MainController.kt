package kr.quidev.main.controller

import kr.quidev.quiz.repository.SkillRepository
import kr.quidev.quiz.service.QuizService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController(
    val quizService: QuizService,
    val skillRepository: SkillRepository,
) {

    @GetMapping("/")
    fun main(model: Model): String {
        val skills = skillRepository.findAll()
        model.addAttribute("skills", skills)
        return "index"
    }
}
