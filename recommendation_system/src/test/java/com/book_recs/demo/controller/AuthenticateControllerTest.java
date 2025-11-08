package test.java.com.book_recs.demo.controller;

import com.book_recs.demo.dto.RegisterUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestMvc
@TestPropertySource(properties = "spring.mail.host=localhost")
@Transactional
public class AuthenticateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SIGNUP_ENDPOINT = "/auth/signup";

    @Test
    void testSignupEndpoint() throws Exception {
        // test successful user registration
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setUsername("apitest");
        registerDto.setEmail("apitest@example.com");
        mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("apitest"))
                .andExpected(jsonPath("$.email").value("apitest@example.com"))
                .andExpect(jsonPath("$.enabled").value(false));
    }

    @Test
    void testSignupWithDuplicateUsername() throws Exception {
        // test duplicate username rejection
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setUsername("duplicate");
        registerDto.setEmail("first@example.com");
        registerDto.setPassword("password123");

        // first signup should succeed
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // second signup with same username should fail
        RegisterUserDto duplicateDto = new RegisterUserDto();
        duplicateDto.setUsername("duplicate");
        duplicateDto.setEmail("second@example.com");
        duplicateDto.setPassword("password123");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateDto)))
                .andExpect(status().isBadRequest());
    }
}
