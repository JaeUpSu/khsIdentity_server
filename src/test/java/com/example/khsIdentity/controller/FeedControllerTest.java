package com.example.khsIdentity.controller;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.mapper.Mapper;
import com.example.khsIdentity.response.FeedResponse;
import com.example.khsIdentity.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
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
public class FeedControllerTest extends RestDocsTestSupport {

    @BeforeEach
    void setUpMocks() {
        User user = new User("김현수", "JaeUpSu","pwjeaupsu123", "jaeupsu@example.com");
        user.setId(1L);

        List<Content> contents = new ArrayList<>();
        Feed feed1 = new Feed(user, "첫 번째 피드입니다.", contents);
        Feed feed2 = new Feed(user, "두 번째 피드입니다.", contents);
        feed1.setId(1L);
        feed2.setId(2L);

        feed1.getContents().add(new Content(feed1, "첫 번째 컨텐츠 입니다."));
        feed1.getContents().add(new Content(feed1, "두 번째 컨텐츠 입니다."));
        feed1.getContents().add(new Content(feed1, "세 번째 컨텐츠 입니다."));

        feed2.getContents().add(new Content(feed2, "첫 번째 컨텐츠 입니다."));

        FeedResponse feedResponse = new FeedResponse(feed1);
        when(feedService.write(any(Feed.class))).thenReturn(feedResponse);
    }

    @Test
    public void post_feed() {
        User user = new User("김현수", "JaeUpSu","pwjeaupsu123", "jaeupsu@example.com");
        user.setId(1L);

        List<Content> contents = new ArrayList<>();
        Feed feed = new Feed(user, "첫 번째 피드입니다.", contents);
        feed.setId(1L);

        Content content1 = new Content(null, "첫 번째 컨텐츠 입니다.");
        Content content2 = new Content(null, "두 번째 컨텐츠 입니다.");
        Content content3 = new Content(null, "세 번째 컨텐츠 입니다.");
        content1.setId(1L);
        content2.setId(2L);
        content3.setId(3L);

        feed.getContents().add(content1);
        feed.getContents().add(content2);
        feed.getContents().add(content3);

        try {
            mockMvc.perform(
                    post("/api/feeds")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson(feed))
            ).andExpect(status().isOk())
                    .andDo(print())
                    .andDo(
                            restDocs.document(
                                    requestFields(
                                            fieldWithPath("id").description("게시글 NO"),
                                            fieldWithPath("title").description("The title of the feed"),
                                            fieldWithPath("user.id").description("The ID of the user posting the feed"),
                                            fieldWithPath("user.name").description("The name of the user"),
                                            fieldWithPath("user.userId").description("The username of the user"),
                                            fieldWithPath("user.password").description("The password of the user"),
                                            fieldWithPath("user.email").description("The email of the user"),
                                            fieldWithPath("user.createdAt").description("The creation timestamp of the user record"),
                                            fieldWithPath("user.isManager").description("Indicates if the user is a manager"),
                                            fieldWithPath("contents[].id").description("The ID of the content"),
                                            fieldWithPath("contents[].body").description("The body text of the content"),
                                            fieldWithPath("contents[].image").description("The image associated with the content, if any"),
                                            fieldWithPath("isPrivate").optional().description("Whether the feed is private"),
                                            fieldWithPath("createdAt").description("The timestamp when the feed was created")
                                    ),
                                    responseFields(
                                            fieldWithPath("id").description("The identifier of the feed"),
                                            fieldWithPath("title").description("The title of the feed"),
                                            fieldWithPath("user.name").description("The name of the user"),
                                            fieldWithPath("user.userId").description("The username of the user"),
                                            fieldWithPath("user.email").description("The email of the user"),
                                            fieldWithPath("user.createdAt").description("The creation timestamp of the user record"),
                                            fieldWithPath("createdAt").description("The timestamp when the feed was created")
                                    )
                            )
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
