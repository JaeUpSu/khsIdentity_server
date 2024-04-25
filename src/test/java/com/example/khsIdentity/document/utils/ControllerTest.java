package com.example.khsIdentity.document.utils;

import com.example.khsIdentity.controller.UserController;
import com.example.khsIdentity.repository.User.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@WebMvcTest({
        UserController.class,
})
public abstract class ControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired protected MockMvc mockMvc;

    @MockBean
    protected UserRepository userRepository;

    protected String createJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}