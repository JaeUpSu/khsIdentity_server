package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.repository.Feed.FeedRepository;
import com.example.khsIdentity.repository.User.UserRepository;
import com.example.khsIdentity.response.FeedResponse;
import com.example.khsIdentity.response.FeedSimpleResponse;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public class FeedService {
    private FeedRepository feedRepository;
    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;

    }

    public FeedSimpleResponse write(Feed feed) {
        User user = feed.getUser();
        feed.setUser(user);

        feedRepository.save(feed);
        FeedSimpleResponse feedResponse = new FeedSimpleResponse(feed);
        return feedResponse;
    }

    @Transactional
    public FeedResponse addContentToFeed(Long feedId, Content content) throws IOException {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedId));

        manageContent(feed, content);

        return new FeedResponse(feed);
    }

    private void manageContent(Feed feed, Content content) {
        content.setFeed(feed);  // Set the relationship from content to feed
        feed.getContents().add(content);  // Add the content to feed's list
    }

    @Transactional(readOnly = true)
    public Optional<Feed> getFeed(Long feedId) {
        return feedRepository.findById(feedId);
    }

    @Transactional(readOnly = true)
    public FeedResponse getFeedResponse(Long feedId) {
        return new FeedResponse(feedRepository.findById(feedId).get());
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long feedId) {
        return feedRepository.findUserById(feedId);
    }

    @Transactional(readOnly = true)
    public List<FeedResponse> getAllFeeds() {
        return feedRepository.findAll().stream()
                .map(FeedResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Feed> getFeedsByContainingTitle(String title) {
        return feedRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Feed> getFeedsByUser(String userId) {
        return feedRepository.findAllByUser_UserId(userId);
    }

    public Feed updatePrivacy(Long feedId, boolean isPrivate) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedId));

        feed.setIsPrivate(isPrivate);
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

        feedRepository.deleteById(feedId);
    }
}
