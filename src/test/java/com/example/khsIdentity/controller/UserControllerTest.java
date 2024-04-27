package com.example.khsIdentity.controller;

import com.example.khsIdentity.dto.LoginRequestDTO;
import com.example.khsIdentity.dto.UserDTO;
import com.example.khsIdentity.mapper.Mapper;
import com.example.khsIdentity.support.docs.RestDocsTestSupport;
import com.example.khsIdentity.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Arrays;
import java.util.Optional;
import static com.example.khsIdentity.config.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class UserControllerTest extends RestDocsTestSupport {

    @BeforeEach
    void setUpMocks() {
        User user0 = new User("김현수", "JaeUpSu","pwjeaupsu123", "jaeupsu@example.com");
        User user1 = new User("JohnDoe", "johndoe123","pwjondoe123", "john.doe@example.com");
        User user2 = new User("AlDoe", "aldoe321","pwaldoe321", "al.doe@example.com");
        User user3 = new User("BonDoe", "bondoe321","pwbondoe321", "bon.doe@example.com");
        
        User user4 = new User("김현수", "JaeUpSu2","pwjeaupsu1223", "jaeupsu2@example.com");
        
        user0.setId(1L);
        user1.setId(2L);
        user2.setId(3L);
        user3.setId(4L);
        user4.setId(5L);

        UserDTO userDTO = new Mapper().convertUserToDto(user1);

        when(userService.join(any(User.class))).thenReturn(userDTO);
        when(userService.findOne(anyLong())).thenReturn(Optional.of(user0));
        when(userService.findOneByUserId(anyString())).thenReturn(Optional.of(user0));
        when(userService.findOneByEmail(anyString())).thenReturn(Optional.of(user0));
        when(userService.findUsers()).thenReturn(Arrays.asList(user0, user1, user2, user3));
        when(userService.findUsersByName(anyString())).thenReturn(Arrays.asList(user0, user4));
        when(userService.getLoggedInUser()).thenReturn(userDTO);
    }

    @Test
    public void user_post() throws Exception {
        User user = new User("김현수", "JaeUpSu","pwjeaupsu123", "jaeupsu@example.com");
        user.setId(1L);

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
                                        fieldWithPath("name").description("The name of the user"),
                                        fieldWithPath("userId").description("The user's unique identifier"),
                                        fieldWithPath("email").description("The email of the user"),
                                        fieldWithPath("createdAt").description("The date and time when the user was created").type("String")
                                )
                        )
                );
    }

    @Test
    public void all_users_get() throws Exception {

        mockMvc.perform(
                        get("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("[].name").description("이름"),
                                        fieldWithPath("[].userId").description("아이디"),
                                        fieldWithPath("[].email").description("이메일"),
                                        fieldWithPath("[].createdAt").description("생성 날짜")
                                )
                        )
                );
    }

    @Test
    public void all_users_byName_get() throws Exception {
        mockMvc.perform(
                        get("/api/users/byName/{name}", "김현수")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("name").description("이름")
                                ),
                                responseFields(
                                        fieldWithPath("[].name").description("이름"),
                                        fieldWithPath("[].userId").description("아이디"),
                                        fieldWithPath("[].email").description("이메일"),
                                        fieldWithPath("[].createdAt").description("생성 날짜")
                                )
                        )
                );
    }

    @Test
    public void user_byUserId_get() throws Exception {
        mockMvc.perform(
                        get("/api/users/byUserId/{userId}", "JaeUpSu")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("userId").description("User Id")
                                ),
                                responseFields(
                                        fieldWithPath("name").description("The name of the user"),
                                        fieldWithPath("userId").description("The user's unique identifier"),
                                        fieldWithPath("email").description("The email of the user"),
                                        fieldWithPath("createdAt").description("The date and time when the user was created").type("String")
                                )
                        )
                );
    }

    @Test
    public void user_byEmail_get() throws Exception {
        mockMvc.perform(
                        get("/api/users/byEmail/{email}", "jaeupsu@email.com")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("email").description("User Email")
                                ),
                                responseFields(
                                        fieldWithPath("name").description("The name of the user"),
                                        fieldWithPath("userId").description("The user's unique identifier"),
                                        fieldWithPath("email").description("The email of the user"),
                                        fieldWithPath("createdAt").description("The date and time when the user was created").type("String").optional()
                                )
                        )
                );
    }

    @Test
    public void user_success_login() throws Exception {
        User user = new User("김현수", "JaeUpSu","pwjeaupsu123", "jaeupsu@example.com");
        user.setId(1L);

        when(userService.login(any(LoginRequestDTO.class))).thenReturn(new Mapper().convertUserToDto(user));
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user.getUserId(), user.getPassword());

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson(loginRequestDTO))
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("userId").description("사용자 ID"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("createdAt").description("생성 날짜")
                                )
                        )
                );
    }

    @Test
    public void user_fail_login() throws Exception {
        User user = new User("김현수", "JaeUpSu","pwjeaupsu123", "jaeupsu@example.com");
        user.setId(1L);

        when(userService.login(any(LoginRequestDTO.class))).thenThrow(new BadCredentialsException("Credentials Invalid"));
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user.getUserId(), user.getPassword());

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson(loginRequestDTO))
                ).andExpect(status().isUnauthorized());
    }

    @Test
    public void user_current_get() throws Exception {
        mockMvc.perform(
                        get("/api/users/current")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("name").description("The name of the user"),
                                        fieldWithPath("userId").description("The user's unique identifier"),
                                        fieldWithPath("email").description("The email of the user"),
                                        fieldWithPath("createdAt").description("The date and time when the user was created").type("String")
                                )
                        )
                );
    }
}
