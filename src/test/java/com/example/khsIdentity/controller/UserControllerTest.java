package com.example.khsIdentity.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void user_get() throws Exception {
        mockMvc.perform(
                get("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(
                        document("user-get",
                                pathParameters(
                                        parameterWithName("id").description("User Generated NO")
                                ),
                                responseFields(
                                        fieldWithPath("userId").description("ID"),
                                        fieldWithPath("name").description("name"),
                                        fieldWithPath("email").description("email")
                                        )
                        )
                );
    }

//    @Test
//    public void user_post() throws Exception {
//        mockMvc.perform(
//                        post("/api/users/joins")
//                                .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(status().isOk())
//                .andDo(
//                        document("user-post",
//                                requestBody(
//
//                                ),
//                                responseFields(
//                                        fieldWithPath("id").description("User Generated NO")
//                                )
//                        )
//                );
//    }
}
