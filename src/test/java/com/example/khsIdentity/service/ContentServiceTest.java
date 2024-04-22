package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.repository.Content.MemoryContentRepository;
import com.example.khsIdentity.repository.Feed.MemoryFeedRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentServiceTest {
    ContentService contentService;
    MemoryContentRepository contentRepository;
    MemoryFeedRepository feedRepository;

    @BeforeEach
    void beforeEach() {
        feedRepository = new MemoryFeedRepository();
        contentRepository = new MemoryContentRepository();
        contentService = new ContentService(contentRepository);
    }

    @AfterEach
    void afterEach() {
        contentRepository.clearStore();
        feedRepository.clearStore();
    }

    @Test
    void 읽기() {
        // given (이런 상황이 주어지면)
        Feed feed = new Feed();
        feedRepository.save(feed);
        Content content = new Content(feed, "create");
        contentRepository.save(content);

        // when (이거를 실행 했을 때)
        Content findContent = contentService.findOne(content.getId()).get();

        // then (이 결과가 기대돼)
        assertThat(findContent.getBody()).isEqualTo("create");
    }

    @Test
    void 수정() {
        // given (이런 상황이 주어지면)
        Feed feed = new Feed();
        feedRepository.save(feed);
        Content content = new Content(feed, "create");
        contentRepository.save(content);

        // when (이거를 실행 했을 때)
        String str1 = "img1";
        byte[] buffers = str1.getBytes();
        contentService.updateContent(content.getId(), "update", buffers);
        Content findContent = contentService.findAllContentByFeed(feed).get(0);

        // then (이 결과가 기대돼)
        assertThat(findContent.getBody()).isEqualTo("update");
    }

    @Test
    void 어떤_Feed_인지_읽기() {
        // given (이런 상황이 주어지면)
        Feed feed = new Feed();
        feedRepository.save(feed);

        // when (이거를 실행 했을 때)
        Content content = new Content(feed, "create");
        contentRepository.save(content);

        // then (이 결과가 기대돼)
        Long feedId = contentService.getFeedIdByContentId(content.getId());
        assertThat(feedId).isEqualTo(feed.getId());
    }

    @Test
    void Feed_제목_읽기() {
        // given (이런 상황이 주어지면)
        Feed feed = new Feed();
        feed.setTitle("제목입니다.");
        feedRepository.save(feed);

        Content content = new Content(feed, "create");
        contentRepository.save(content);

        // when (이거를 실행 했을 때)
        String feedTitle = contentService.getFeedTitleByContentId(content.getId());

        // then (이 결과가 기대돼)
        assertThat(feedTitle).isEqualTo("제목입니다.");
    }
}

