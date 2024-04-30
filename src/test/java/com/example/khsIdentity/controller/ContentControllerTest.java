package com.example.khsIdentity.controller;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ContentControllerTest extends RestDocsTestSupport {

    @BeforeEach
    void setUpMocks() throws IOException {
        Feed feed = new Feed();
        feed.setId(1L);
        Content content1 = new Content(feed, "첫 컨텐츠입니다.");
        content1.setId(1L);
        Content content2 = new Content(feed, "두 번째 컨텐츠입니다.");
        content2.setId(2L);

        List<Content> contents = new ArrayList<>();
        contents.add(content1);
        contents.add(content2);

        when(contentService.findAll()).thenReturn(contents);
        when(contentService.findOne(anyLong())).thenReturn(Optional.ofNullable(content1));


    }

    @Test
    public void get_all_contents () throws Exception {
        mockMvc.perform(
                        get("/api/contents")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("[].id").description("The unique identifier of the content"),
                                        fieldWithPath("[].body").description("The body text of the content"),
                                        fieldWithPath("[].image").description("The binary data of the content's image, encoded in Base64").optional()
                                )
                        )
                );
    }

    @Test
    public void get_one_content() throws Exception  {
        mockMvc.perform(
                        get("/api/contents/{contentId}",1L)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                  parameterWithName("contentId").description("The unique identifier of the content")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The unique identifier of the content"),
                                        fieldWithPath("body").description("The body text of the content"),
                                        fieldWithPath("image").description("The binary data of the content's image, encoded in Base64").optional()
                                )
                        )
                );
    }

    @Test
    public void put_content_body () throws Exception {
        Feed feed = new Feed();
        feed.setId(1L);
        Content content1 = new Content(feed, "첫 컨텐츠입니다.");
        content1.setId(1L);
        content1.setBody("업데이트된 내용입니다.");
        when(contentService.updateBody(anyLong(),anyString())).thenReturn(content1);

        mockMvc.perform(
                        put("/api/contents/{contentId}/body",1L)
                                .queryParam("body","업데이트된 내용입니다.")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("contentId").description("The unique identifier of the content")
                                ),
                                queryParameters(
                                        parameterWithName("body").description("The body text of the content")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The unique identifier of the content"),
                                        fieldWithPath("body").description("The body text of the content"),
                                        fieldWithPath("image").description("The binary data of the content's image, encoded in Base64").optional()
                                )
                        )
                );
    }

    @Test
    public void upload_content_image () throws Exception {
        Feed feed = new Feed();
        feed.setId(1L);
        Content content1 = new Content(feed, "첫 컨텐츠입니다.");
        content1.setId(1L);

        byte[] exampleImageData = new byte[] { (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, // PNG header
                0x00, 0x00, 0x00, 0x0D, 'I', 'H', 'D', 'R',
        };
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                exampleImageData
        );
        content1.setImage(exampleImageData);
        when(contentService.updateImage(anyLong(), any(byte[].class))).thenReturn(content1);

        mockMvc.perform(multipart("/api/contents/{contentId}/image",1L)
                                .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("contentId").description("The unique identifier of the content")
                                ),
                               requestParts(
                                        partWithName("image").description("The binary data of the content's image, encoded in Base64")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The unique identifier of the content"),
                                        fieldWithPath("body").description("The body text of the content"),
                                        fieldWithPath("image").description("The binary data of the content's image, encoded in Base64").optional()
                                )
                        )
                );
    }

    @Test
    public void delete_content () throws Exception {
        mockMvc.perform(
                        delete("/api/contents/{contentId}",1L)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("contentId").description("The unique identifier of the content")
                                )
                        )
                );
    }
}
