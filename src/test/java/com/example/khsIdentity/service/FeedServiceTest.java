package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.repository.Feed.MemoryFeedRepository;
import com.example.khsIdentity.repository.User.MemoryUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedServiceTest {

    FeedService feedService;

    MemoryFeedRepository feedRepository;
    MemoryUserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        feedRepository = new MemoryFeedRepository();
        userRepository = new MemoryUserRepository();
        feedService = new FeedService(feedRepository, userRepository);
    }

    @AfterEach
    void afterEach() {
        feedRepository.clearStore();
    }

    @Test
    void 컨텐츠_추가 () throws IOException {
        // given
        User user = new User("mememe12","mememe12","mememe12","mememe12@google.co.kr");
        List<Content> contents = new ArrayList<>();
        Feed feed = new Feed(user, "title", contents);
        Long id = feedService.write(feed);

        // when
        MultipartFile file = new MockMultipartFile("img", "img".getBytes());
        Content newContent = new Content(feed, "newContents");
        newContent.setImage(file.getBytes());
        Feed updatedFeed = feedService.addContentToFeed(id, newContent);

        // then
        assertThat(newContent).isEqualTo(updatedFeed.getContents().get(0));
    }

    @Test
    void 글쓴이_반환 () {
        // given
        User user = new User("mememe12","mememe12","mememe12","mememe12@google.co.kr");
        List<Content> contents = new ArrayList<>();
        Feed feed = new Feed(user, "title", contents);
        Long id = feedService.write(feed);

        // when
        User user1 = feedService.getUserById(id).get();

        // then
        assertThat(user1.getUserId()).isEqualTo(user.getUserId());
    }
    
    @Test
    void 제목에_문자열_탐색_포함_모든피드_반환() {
        // given
        User user = new User("mememe12","mememe12","mememe12","mememe12@google.co.kr");
        List<Content> contents = new ArrayList<>();
        Feed feed1 = new Feed(user, "포함", contents);
        Feed feed2 = new Feed(user, "포함", contents);
        Feed feed3 = new Feed(user, "Nothing", contents);
        Feed feed4 = new Feed(user, "포함", contents);
        feedService.write(feed1);
        feedService.write(feed2);
        feedService.write(feed3);
        feedService.write(feed4);

        // when
        List<Feed> feeds = feedService.getFeedsByContainingTitle("포함");

        // then
        assertThat(feeds.size()).isEqualTo(3);
    }

    @Test
    void 글쓴이가_작성한_모든글_반환 () {
        // given
        User user = new User("mememe12","mememe12","mememe12","mememe12@google.co.kr");
        User anybody = new User("mememe33","mememe33","mememe33","mememe33@google.co.kr");
        List<Content> contents = new ArrayList<>();
        Feed feed1 = new Feed(user, "title", contents);
        Feed feed2 = new Feed(anybody, "title", contents);
        Feed feed3 = new Feed(anybody, "title", contents);
        Feed feed4 = new Feed(user, "title", contents);
        feedService.write(feed1);
        feedService.write(feed2);
        feedService.write(feed3);
        feedService.write(feed4);

        // when
        List<Feed> feeds = feedService.getFeedsByUser("mememe12");

        // then
        assertThat(feeds.size()).isEqualTo(2);
    }

    @Test
    void 비공개_설정 () {
        // given
        User user = new User("mememe12","mememe12","mememe12","mememe12@google.co.kr");
        List<Content> contents = new ArrayList<>();
        Feed feed = new Feed(user, "title", contents);
        Long id = feedService.write(feed);

        // when
        Feed nextFeed = feedService.updatePrivacy(id, true);

        // then
        assertThat(nextFeed.getPrivate()).isEqualTo(true);
    }

    @Test
    void 제목_수정 () {
        // given
        User user = new User("mememe12","mememe12","mememe12","mememe12@google.co.kr");
        List<Content> contents = new ArrayList<>();
        Feed feed = new Feed(user, "title", contents);
        Long id = feedService.write(feed);

        // when
        Feed nextFeed = feedService.updateTitleFeed(id, "new Title");

        // then
        assertThat(nextFeed.getTitle()).isEqualTo("new Title");
    }

    @Test
    void 삭제 () {
        // given
        User user = new User("mememe12","mememe12","mememe12","mememe12@google.co.kr");
        User anybody = new User("mememe33","mememe33","mememe33","mememe33@google.co.kr");
        List<Content> contents = new ArrayList<>();
        Feed feed1 = new Feed(user, "title", contents);
        Feed feed2 = new Feed(anybody, "title", contents);
        Feed feed3 = new Feed(anybody, "title", contents);
        Feed feed4 = new Feed(user, "title", contents);
        feedService.write(feed1);
        feedService.write(feed2);
        feedService.write(feed3);
        feedService.write(feed4);

        // when
        feedService.deleteFeed(feed3.getId());
        List<Feed> feeds = feedService.getAllFeeds();

        // then
        assertThat(feeds.size()).isEqualTo(3);
    }
}
