package com.example.khsIdentity.repository;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.repository.Feed.MemoryFeedRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryFeedRepositoryTest {

    MemoryFeedRepository repository = new MemoryFeedRepository();

    @AfterEach
    void afterEach() {
        repository.clearStore();
    }

    @Test
    void save(){
        User user = new User();
        List<Content> contents = new ArrayList<>();

        Feed feed = new Feed(user, "Title", contents);
        repository.save(feed);

        Feed result = repository.findById(feed.getId()).get();
        assertThat(feed).isEqualTo(result);
    }

    @Test
    void 문자열_제목에서_포함한_피드_리스트_반환(){
        User user = new User();
        List<Content> contents = new ArrayList<>();

        Feed feed1 = new Feed(user, "Title!", contents);
        Feed feed2 = new Feed(user, "NotTitle", contents);
        Feed feed3 = new Feed(user, "Title!", contents);
        repository.save(feed1);
        repository.save(feed2);
        repository.save(feed3);

        List<Feed> feeds = repository.findByTitleContainingIgnoreCase("Title!");
        assertThat(feeds.size()).isEqualTo(2);
    }

    @Test
    void 삭제(){
        User user = new User();
        List<Content> contents = new ArrayList<>();

        Feed feed1 = new Feed(user, "Title!", contents);
        Feed feed2 = new Feed(user, "NotTitle", contents);
        Feed feed3 = new Feed(user, "Title!", contents);
        repository.save(feed1);
        repository.save(feed2);
        repository.save(feed3);

        repository.delete(feed2.getId());

        List<Feed> feeds = repository.findAll();
        assertThat(feeds.size()).isEqualTo(2);
    }

    @Test
    void 글쓴이_반환(){
        User user = new User();
        List<Content> contents = new ArrayList<>();

        Feed feed1 = new Feed(user, "Title!", contents);
        Feed feed2 = new Feed(user, "NotTitle", contents);
        Feed feed3 = new Feed(user, "Title!", contents);
        repository.save(feed1);
        repository.save(feed2);
        repository.save(feed3);

        repository.delete(feed2.getId());

        User result = repository.findUserById(feed1.getId()).get();
        assertThat(result.getId()).isEqualTo(user.getId());
    }

    @Test
    void 특정_글쓴이_모든_피드_반환(){
        User user = new User("spring00", "spring00", "spring00", "spring00@naver.com");
        List<Content> contents = new ArrayList<>();

        Feed feed1 = new Feed(user, "Title!", contents);
        Feed feed2 = new Feed(user, "NotTitle", contents);
        Feed feed3 = new Feed(user, "Title!", contents);
        repository.save(feed1);
        repository.save(feed2);
        repository.save(feed3);

        List<Feed> result = repository.findAllByUserId(user.getUserId());

        System.out.println(result.size());
        assertThat(result.size()).isEqualTo(3);
    }
}
