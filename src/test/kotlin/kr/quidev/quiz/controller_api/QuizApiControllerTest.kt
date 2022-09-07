package kr.quidev.quiz.controller_api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.quidev.quiz.domain.entity.QuizCreateDto
import org.hamcrest.Matchers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
internal class QuizApiControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("create quiz test: expected situation")
    fun createQuiz() {
        val quizCreateDto = QuizCreateDto(
            description = "desc",
            answer = "answer",
            explanation = "explanation",
            examples = arrayOf("example1", "example2", "example3")
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/quiz/new")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("shane"))
                .content(jacksonObjectMapper().writeValueAsString(quizCreateDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"description\":\"desc\"")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.answer").value("answer"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.explanation", Matchers.containsString("explanation")))
    }

    @Test
    @DisplayName("create quiz test: Description is not provided")
    fun createQuizNoDesc() {
        val quizCreateDto = QuizCreateDto(
            description = "",
            answer = "answer",
            explanation = "explanation",
            examples = arrayOf("example1", "example2", "example3")
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/quiz/new")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("shane"))
                .content(jacksonObjectMapper().writeValueAsString(quizCreateDto))
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

}
