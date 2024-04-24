package com.example.khsIdentity.controller;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/feeds")
public class FeedController {

    private FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    };


    @GetMapping
    public ResponseEntity<List<Feed>> getAllFeeds() {
        return ResponseEntity.ok(feedService.getAllFeeds());
    }

    @PostMapping
    public ResponseEntity<Long> createFeed(@RequestBody Feed feed) {
        if (feed.getContents() != null) {
            for (Content content : feed.getContents()) {
                content.setFeed(feed);  // Manually setting the back-reference
            }
        }

        return ResponseEntity.ok(feedService.write(feed));
    }

    @PostMapping("/{feedId}/contents")
    public ResponseEntity<Feed> addContentToFeed(@PathVariable Long feedId,
                                                    @RequestBody Content content) throws IOException {
        Feed feed = feedService.addContentToFeed(feedId, content);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<Feed> getFeed(@PathVariable Long feedId) {
        return feedService.getFeed(feedId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Feed>> searchFeedsByTitle(@RequestParam String title) {
        List<Feed> feeds = feedService.getFeedsByContainingTitle(title);
        return ResponseEntity.ok(feeds);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feed>> getFeedsByUser(@PathVariable String userId) {
        List<Feed> feeds = feedService.getFeedsByUser(userId);
        return ResponseEntity.ok(feeds);
    }


    @PatchMapping("/{feedId}/privacy")
    public ResponseEntity<Feed> updateFeedPrivacy(@PathVariable Long feedId, @RequestParam boolean isPrivate) {
        Feed updatedFeed = feedService.updatePrivacy(feedId, isPrivate);
        return ResponseEntity.ok(updatedFeed);
    }

    @PatchMapping("/{feedId}/title")
    public ResponseEntity<Feed> updateFeedTitle(@PathVariable Long feedId, @RequestParam String title) {
        Feed updatedFeed = feedService.updateTitleFeed(feedId, title);
        return ResponseEntity.ok(updatedFeed);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.ok().build();
    }
}
