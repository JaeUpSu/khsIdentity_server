package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.repository.Feed.FeedRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Transactional
public class FeedService {
    private FeedRepository feedRepository;
    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public Long write(Feed feed) {
        feedRepository.save(feed);
        return feed.getId();
    }

    public Content addContentToFeed(Long feedId, String body, MultipartFile file) throws IOException {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedId));

        Content newContent = new Content();
        newContent.setFeed(feed);
        newContent.setBody(body);
        if (file != null && !file.isEmpty()) {
            newContent.setImage(file.getBytes());
        }

        feed.getContents().add(newContent);
        feedRepository.save(feed);
        return newContent;
    }

    @Transactional(readOnly = true)
    public Optional<Feed> getFeed(Long feedId) {
        return feedRepository.findById(feedId);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long feedId) {
        return feedRepository.findUserById(feedId);
    }

    @Transactional(readOnly = true)
    public List<Feed> getAllFeeds() {
        return feedRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Feed> getFeedsByContainingTitle(String title) {
        return feedRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Feed> getFeedsByUser(String userId) {
        return feedRepository.findAllByUserId(userId);
    }

    public Feed updatePrivacy(Long feedId, boolean isPrivate) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedId));

        feed.setPrivate(isPrivate);
        return feedRepository.save(feed);
    }

    public Feed updateTitleFeed(Long feedId, String title) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedId));

        feed.setTitle(title);
        return feedRepository.save(feed);
    }

    public void deleteFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedId));

        feedRepository.delete(feedId);
    }
}
