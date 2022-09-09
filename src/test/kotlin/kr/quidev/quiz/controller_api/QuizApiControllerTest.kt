package kr.quidev.quiz.controller_api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.quidev.quiz.domain.entity.QuizCreateDto
import org.hamcrest.Matchers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
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
    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    @DisplayName("create quiz test: expected situation")
    fun createQuiz() {
        val quizCreateDto = QuizCreateDto(
            description = "desc",
            answer = "answer",
            explanation = "explanation",
            examples = arrayOf("example1", "example2", "example3")
        )
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/quiz/new")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user("shane"))
                .content(jacksonObjectMapper().writeValueAsString(quizCreateDto))
        )
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"description\":\"desc\"")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.body.answer").value("answer"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.body.explanation", Matchers.containsString("explanation")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("200"))

        log.info(result.andReturn().response.contentAsString)
    }

    @Test
    @DisplayName("create quiz test: Description is not provided")
    fun createQuizNoDesc() {
        for (description in arrayOf("", " ", null)) {
            val quizCreateDto = QuizCreateDto(
                description = description,
                answer = "answer",
                explanation = "explanation",
                examples = arrayOf("example1", "example2", "example3")
            )
            val result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/quiz/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(SecurityMockMvcRequestPostProcessors.user("shane"))
                    .content(jacksonObjectMapper().writeValueAsString(quizCreateDto))
            )
            result.andExpect(MockMvcResultMatchers.jsonPath("$.body").isEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))

            log.info(result.andReturn().response.contentAsString)
        }
    }

}
