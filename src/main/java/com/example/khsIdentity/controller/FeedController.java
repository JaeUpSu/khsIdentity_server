package com.example.khsIdentity.controller;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.response.FeedResponse;
import com.example.khsIdentity.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/feeds")
public class FeedController {

    private FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    };


    @GetMapping
    public ResponseEntity<List<FeedResponse>> getAllFeeds() {
        List<FeedResponse> feeds = feedService.getAllFeeds().stream()
                .map(FeedResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feeds);
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
    public ResponseEntity<FeedResponse> addContentToFeed(@PathVariable Long feedId,
                                                    @RequestBody Content content) throws IOException {
        FeedResponse feed = new FeedResponse(feedService.addContentToFeed(feedId, content));
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponse> getFeed(@PathVariable Long feedId) {
        Feed feed = feedService.getFeed(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found"));
        FeedResponse response = new FeedResponse(feed);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FeedResponse>> searchFeedsByTitle(@RequestParam String title) {
        List<FeedResponse> feeds = feedService.getFeedsByContainingTitle(title)
                .stream().map(FeedResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feeds);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedResponse>> getFeedsByUser(@PathVariable String userId) {
        List<FeedResponse> feeds = feedService.getFeedsByUser(userId)
                .stream().map(FeedResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feeds);
    }


    @PatchMapping("/{feedId}/privacy")
    public ResponseEntity<FeedResponse> updateFeedPrivacy(@PathVariable Long feedId, @RequestParam boolean isPrivate) {
        FeedResponse updatedFeed = new FeedResponse(feedService.updatePrivacy(feedId, isPrivate));
        return ResponseEntity.ok(updatedFeed);
    }

    @PatchMapping("/{feedId}/title")
    public ResponseEntity<FeedResponse> updateFeedTitle(@PathVariable Long feedId, @RequestParam String title) {
        FeedResponse updatedFeed = new FeedResponse(feedService.updateTitleFeed(feedId, title));
        return ResponseEntity.ok(updatedFeed);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.ok().build();
    }
}
