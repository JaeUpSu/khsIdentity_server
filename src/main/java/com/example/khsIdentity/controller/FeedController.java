package com.example.khsIdentity.controller;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.response.FeedResponse;
import com.example.khsIdentity.response.FeedSimpleResponse;
import com.example.khsIdentity.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feeds")
public class FeedController {

    private FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    };


    @GetMapping
    public ResponseEntity<List<FeedResponse>> getAllFeeds() {
        List<FeedResponse> feeds = feedService.getAllFeeds();
        return ResponseEntity.ok(feeds);
    }

    @PostMapping
    public ResponseEntity<FeedSimpleResponse> createFeed(@RequestBody Feed feed) {
        return ResponseEntity.ok(feedService.write(feed));
    }

    @PatchMapping("/{feedId}/contents")
    public ResponseEntity<FeedResponse> addContentToFeed(@PathVariable("feedId") Long feedId,
                                                    @RequestBody Content content) throws IOException {
        FeedResponse feed = feedService.addContentToFeed(feedId, content);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponse> getFeed(@PathVariable("feedId") Long feedId) {
        FeedResponse feed = feedService.getFeedResponse(feedId);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<FeedResponse>> searchFeedsByTitle(@PathVariable("title") String title) {
        List<FeedResponse> feeds = feedService.getFeedsByContainingTitle(title);
        return ResponseEntity.ok(feeds);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedResponse>> getFeedsByUser(@PathVariable("userId") String userId) {
        List<FeedResponse> feeds = feedService.getFeedsByUser(userId);
        return ResponseEntity.ok(feeds);
    }


    @PatchMapping("/{feedId}/privacy")
    public ResponseEntity<FeedResponse> updateFeedPrivacy(@PathVariable("feedId") Long feedId, @RequestParam("isPrivate") boolean isPrivate) {
            FeedResponse feed = feedService.updatePrivacy(feedId, isPrivate);
            return ResponseEntity.ok(feed);
    }

    @PatchMapping("/{feedId}/title")
    public ResponseEntity<FeedResponse> updateFeedTitle(@PathVariable("feedId") Long feedId, @RequestParam("title") String title) {
        FeedResponse updatedFeed = feedService.updateTitleFeed(feedId, title);
        return ResponseEntity.ok(updatedFeed);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(@PathVariable("feedId") Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.ok().build();
    }
}
