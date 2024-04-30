package com.example.khsIdentity.repository;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.repository.Content.MemoryContentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryContentRepositoryTest {

    MemoryContentRepository repository = new MemoryContentRepository();

    @AfterEach
    void afterEach() {
        repository.clearStore();
    }

    @Test
    void save() {
        Feed feed = new Feed();
        feed.setId(1L);
        String body = "This is a test";
        Content content = new Content(feed, body);

        repository.save(content);

        Content result = repository.findById(content.getId()).get();

        assertThat(content).isEqualTo(result);
    }


    @Test
    void findByFeed(){
        Feed feed = new Feed();
        feed.setId(1L);
        String body1 = "This is a test1";
        String body2 = "This is a test2";

        Content content1 = new Content(feed, body1);
        Content content2 = new Content(feed, body2);

        repository.save(content1);
        repository.save(content2);

        Content result = repository.findByFeed(feed).get(1);

        assertThat(result).isEqualTo(content2);
    }

    @Test
    void delete() {
        Feed feed = new Feed();
        feed.setId(1L);
        String body1 = "This is a test1";
        String body2 = "This is a test2";

        Content content1 = new Content(feed, body1);
        Content content2 = new Content(feed, body2);

        repository.save(content1);
        repository.save(content2);
        repository.deleteById(content1.getId());

        Content result = repository.findByFeed(content2.getFeed()).get(0);

        assertThat(result.getBody()).isEqualTo("This is a test2");
    }
}
