package kr.quidev.quiz.controller_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.quidev.member.domain.entity.Member;
import kr.quidev.member.service.MemberService;
import kr.quidev.quiz.domain.dto.QuizCreateDto;
import kr.quidev.quiz.domain.entity.Skill;
import kr.quidev.quiz.service.QuizService;
import kr.quidev.quiz.service.SkillService;
import kr.quidev.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Use java test code instead
 * until https://youtrack.jetbrains.com/issue/KT-22208 is fixed
 * <p>
 * ref: https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class QuizApiControllerTestJava implements UserDetailsService {

    @Autowired
    QuizService quizService;

    @Autowired
    SkillService skillService;

    @Autowired
    MemberService memberService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void createQuiz() throws Exception {
        memberService.createMember(new Member(null, "pw", "name", "email", "role"));
        UserDetails user = customUserDetailsService.loadUserByUsername("email");

        Skill skill = skillService.save(new Skill(null, null, "java"));
        QuizCreateDto quizCreateDto = new QuizCreateDto("desc", "answ", "expl", skill.getId(), new String[]{"ex1", "ex2", "ex3"});

        ResultActions result = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build().perform(
                        MockMvcRequestBuilders.post("/api/quiz")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.user(user))
                                .content(mapper.writeValueAsString(quizCreateDto))
                );

        result.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User("username", "pw", Collections.EMPTY_LIST);
    }
}
