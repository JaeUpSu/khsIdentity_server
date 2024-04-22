package com.example.khsIdentity.controller;

import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.service.FeedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedService feedService;

    @Test
    public void createFeedExample() throws Exception {
        Feed feed = new Feed();
        feed.setTitle("New Feed");

        when(feedService.write(any(Feed.class))).thenReturn(1L);

        this.mockMvc.perform(post("/api/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Feed\"}"))
                .andExpect(status().isOk())
                .andDo(document("create-feed", responseFields(
                        fieldWithPath("id").description("The ID of the created feed")
                )));
    }
}
