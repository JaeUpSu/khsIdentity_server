package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.repository.Feed.FeedRepository;
import com.example.khsIdentity.repository.User.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Transactional
public class FeedService {
    private FeedRepository feedRepository;
    private UserRepository userRepository;
    public FeedService(FeedRepository feedRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;

    }

    public Long write(Feed feed) {
        User user = feed.getUser();
        if (user != null) {
            if (user.getId() == null) {
                userRepository.save(user);
            } else {
                user = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
            }
            feed.setUser(user);
        }

        feedRepository.save(feed);
        return feed.getId();
    }

    @Transactional
    public Feed addContentToFeed(Long feedId, Content content) throws IOException {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + feedId));

        manageContent(feed, content);

        return feed;
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
        return feedRepository.findAllByUser_UserId(userId);
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

        feedRepository.deleteById(feedId);
    }
}
