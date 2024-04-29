package com.example.khsIdentity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.service.ContentService;
import com.example.khsIdentity.service.FeedService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController {

    private final ContentService contentService;
    private final FeedService feedService;

    @Autowired
    public ContentController(ContentService contentService, FeedService feedService) {
        this.contentService = contentService;
        this.feedService = feedService;
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<Content> getContentById(@PathVariable Long contentId) {
        return contentService.findOne(contentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<Content> updateContent(
            @PathVariable Long contentId,
            @RequestParam("data") String newData,
            @RequestParam("file") MultipartFile file) throws IOException {
        byte[] newImage = file.isEmpty() ? null : file.getBytes();
        Content updatedContent = contentService.updateContent(contentId, newData, newImage);
        return ResponseEntity.ok(updatedContent);
    }

    @GetMapping("/feed/{feedId}")
    public ResponseEntity<List<Content>> getAllContentsByFeed(@PathVariable Long feedId) {
        Feed feed = feedService.getFeed(feedId).get();
        List<Content> contents = contentService.findAllContentByFeed(feed);
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/feedId/{contentId}")
    public ResponseEntity<Long> getFeedIdByContentId(@PathVariable Long contentId) {
        Long feedId = contentService.getFeedIdByContentId(contentId);
        return ResponseEntity.ok(feedId);
    }

    @GetMapping("/feedTitle/{contentId}")
    public ResponseEntity<String> getFeedTitleByContentId(@PathVariable Long contentId) {
        String title = contentService.getFeedTitleByContentId(contentId);
        return ResponseEntity.ok(title);
    }
}
