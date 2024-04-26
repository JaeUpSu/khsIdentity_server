package com.example.khsIdentity.controller;

import com.example.khsIdentity.dto.UserDTO;
import com.example.khsIdentity.support.docs.RestDocsTestSupport;
import com.example.khsIdentity.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.khsIdentity.config.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class UserControllerTest extends RestDocsTestSupport {

    @BeforeEach
    void setUpMocks() {
        User mockUser = new User("Test User","TEST","TEST1234", "test@example.com");
        mockUser.setId(1L);
        when(userService.findOne(1L)).thenReturn(Optional.of(mockUser));
    }

    @Test
    public void user_get() throws Exception {
        mockMvc.perform(
                get("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                       restDocs.document(
                                pathParameters(
                                        parameterWithName("id").description("User Generated NO")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The ID of the user"),
                                        fieldWithPath("name").description("The name of the user"),
                                        fieldWithPath("password").description("The user's password"),
                                        fieldWithPath("userId").description("The user's unique identifier"),
                                        fieldWithPath("email").description("The email of the user"),
                                        fieldWithPath("createdAt").description("The date and time when the user was created").type("String").optional(),
                                        fieldWithPath("isManager").description("Manager Authority")
                                )
                        )
                );
    }

    @Test
    public void user_post() throws Exception {
        User user = new User("김현수","jaeupsu","qlalfqjsgh","jaeupsu@email.com");
        user.setId(2L);

        // thenReturn(userDto)는 join() 메서드가 호출될 때 마다
        // userDto 객체를 반환하도록 설정
        // 따라서 테스트 코드에서 userService.join(any(User.class))가
        // 호출되면 항상 userDto가 반환

        UserDTO userDto = new UserDTO(user.getName(), user.getUserId(), user.getEmail(), LocalDateTime.now());
        when(userService.join(any(User.class))).thenReturn(userDto);

        mockMvc.perform(
                        post("/api/users/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson(user))
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("id").description("User Generated NO").type("Long"),
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("userId").description("사용자 ID"),
                                        fieldWithPath("password").description("비밀번호").attributes(field("constraints", "길이 8자 이상")),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("createdAt").description("생성 날짜"),
                                        fieldWithPath("isManager").description("관리자 권한")
                                ),
                                responseFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("userId").description("사용자 ID"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("createdAt").description("생성 날짜")
                                        )
                        )
                );
    }
}
