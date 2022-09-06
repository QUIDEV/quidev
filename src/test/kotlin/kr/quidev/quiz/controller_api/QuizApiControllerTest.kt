package kr.quidev.quiz.controller_api

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
    fun createQuiz() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/quiz/new")
                .with(SecurityMockMvcRequestPostProcessors.user("shane"))
                .param("desc", "desc")
                .param("answer", "answer")
                .param("explanation", "explanation")
                .param("examples", "example1")
                .param("examples", "example2")
                .param("examples", "example3")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"description\":\"desc\"")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.answer").value("answer"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.explanation", Matchers.containsString("explanation")))
    }
}
