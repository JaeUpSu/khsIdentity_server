package com.example.khsIdentity.controller;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.response.FeedResponse;
import com.example.khsIdentity.response.FeedSimpleResponse;
import com.example.khsIdentity.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    void setUpMocks() throws IOException {
        User user = new User("김현수", "JaeUpSu","pwjeaupsu123", "jaeupsu@example.com");
        user.setId(1L);

        List<Content> contents1 = new ArrayList<>();
        List<Content> contents2 = new ArrayList<>();
        Feed feed1 = new Feed(user, "첫 번째 피드입니다.", contents1);
        Feed feed2 = new Feed(user, "두 번째 피드입니다.", contents2);
        feed1.setId(1L);
        feed2.setId(2L);

        feed1.getContents().add(new Content(feed1, "첫 번째 컨텐츠 입니다."));
        feed1.getContents().add(new Content(feed1, "두 번째 컨텐츠 입니다."));
        feed1.getContents().add(new Content(feed1, "세 번째 컨텐츠 입니다."));
        feed1.getContents().get(0).setId(1L);
        feed1.getContents().get(1).setId(2L);
        feed1.getContents().get(2).setId(3L);

        feed2.getContents().add(new Content(feed2, "첫 번째 컨텐츠 입니다."));
        feed2.getContents().get(0).setId(1L);

        List<FeedResponse> feeds = new ArrayList<>();
        feeds.add(new FeedResponse(feed1));
        feeds.add(new FeedResponse(feed2));

        List<FeedResponse> feedResponses = new ArrayList<>();
        feedResponses.add(new FeedResponse(feed1));
        feedResponses.add(new FeedResponse(feed2));

        FeedSimpleResponse feedSimpleResponse = new FeedSimpleResponse(feed1);

        Content newContent = new Content(feed2, "추가한 컨텐츠입니다.");
        newContent.setId(2L);

        feed2.getContents().add(newContent);

        when(feedService.addContentToFeed(any(Long.class), any(Content.class))).thenReturn(new FeedResponse(feed2));
        when(feedService.write(any(Feed.class))).thenReturn(feedSimpleResponse);
        when(feedService.getAllFeeds()).thenReturn(feeds);
        when(feedService.getFeedResponse(anyLong())).thenReturn(feedResponses.get(0));
    }

    @Test
    public void post_feed() throws Exception {
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
                                            fieldWithPath("username").description("The name of the user"),
                                            fieldWithPath("createdAt").description("The timestamp when the feed was created")
                                    )
                            )
                    );

    }

    @Test
    public void get_all_feeds() throws Exception {
        mockMvc.perform(
                        get("/api/feeds")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("[].id").description("The identifier of the feed"),
                                        fieldWithPath("[].title").description("The title of the feed"),
                                        fieldWithPath("[].user.name").description("The name of the user"),
                                        fieldWithPath("[].user.userId").description("The username of the user"),
                                        fieldWithPath("[].user.email").description("The email of the user"),
                                        fieldWithPath("[].user.createdAt").description("The creation timestamp of the user record"),
                                        fieldWithPath("[].contents[].id").description("The email of the user"),
                                        fieldWithPath("[].contents[].body").description("The email of the user"),
                                        fieldWithPath("[].contents[].image").description("The creation timestamp of the user record"),
                                        fieldWithPath("[].createdAt").description("The timestamp when the feed was created")
                                )
                        )
                );
    }

    @Test
    public void get_feed() throws Exception {
        mockMvc.perform(
                        get("/api/feeds/{feedId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("feedId").description("Generated Feed NO")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The identifier of the feed"),
                                        fieldWithPath("title").description("The title of the feed"),
                                        fieldWithPath("user.name").description("The name of the user"),
                                        fieldWithPath("user.userId").description("The username of the user"),
                                        fieldWithPath("user.email").description("The email of the user"),
                                        fieldWithPath("user.createdAt").description("The creation timestamp of the user record"),
                                        fieldWithPath("contents[].id").description("The email of the user"),
                                        fieldWithPath("contents[].body").description("The email of the user"),
                                        fieldWithPath("contents[].image").description("The creation timestamp of the user record"),
                                        fieldWithPath("createdAt").description("The timestamp when the feed was created")
                                )
                        )
                );
    }

    @Test
    public void add_content_to_feed() throws Exception {
        User user = new User("김현수", "JaeUpSu","pwjeaupsu123", "jaeupsu@example.com");
        user.setId(1L);

        List<Content> contents2 = new ArrayList<>();
        Feed feed2 = new Feed(user, "두 번째 피드입니다.", contents2);

        feed2.getContents().add(new Content(feed2, "첫 번째 컨텐츠 입니다."));
        feed2.getContents().get(0).setId(1L);

        Content newContent = new Content(feed2, "추가한 컨텐츠입니다.");
        newContent.setId(2L);

        mockMvc.perform(
                        post("/api/feeds/{feedId}/contents", 2L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson(newContent))
                ).andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("feedId").description("Generated Feed NO")
                                ),
                                requestFields(
                                        fieldWithPath("id").description("The identifier of the content"),
                                        fieldWithPath("body").description("The email of the user"),
                                        fieldWithPath("image").description("The creation timestamp of the user record")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The identifier of the feed"),
                                        fieldWithPath("title").description("The title of the feed"),
                                        fieldWithPath("user.name").description("The name of the user"),
                                        fieldWithPath("user.userId").description("The username of the user"),
                                        fieldWithPath("user.email").description("The email of the user"),
                                        fieldWithPath("user.createdAt").description("The creation timestamp of the user record"),
                                        fieldWithPath("contents[].id").description("The email of the user"),
                                        fieldWithPath("contents[].body").description("The email of the user"),
                                        fieldWithPath("contents[].image").description("The creation timestamp of the user record"),
                                        fieldWithPath("createdAt").description("The timestamp when the feed was created")
                                )
                        )
                );
    }
}
