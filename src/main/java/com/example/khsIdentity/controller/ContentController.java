package com.example.khsIdentity.controller;

import com.example.khsIdentity.response.FeedResponse;
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

    @GetMapping
    public ResponseEntity<List<Content>> getContents() {
        return ResponseEntity.ok(contentService.findAll());
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<Content> getContentById(@PathVariable("contentId") Long contentId) {
        return contentService.findOne(contentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{contentId}/body")
    public ResponseEntity<Content> updateContentBody(
            @PathVariable("contentId") Long contentId,
            @RequestParam("body") String newBody) throws IOException {
        Content updatedContent = contentService.updateBody(contentId, newBody);
        return ResponseEntity.ok(updatedContent);
    }
    @PostMapping("/{contentId}/image")
    public ResponseEntity<Content> uploadContentImage(
            @PathVariable("contentId") Long contentId,
            @RequestParam("image") MultipartFile image) throws IOException {
        byte[] newImage = image.isEmpty() ? null : image.getBytes();
        Content updatedContent = contentService.updateImage(contentId, newImage);
        return ResponseEntity.ok(updatedContent);
    }

    @DeleteMapping("{contentId}")
    public ResponseEntity<Void> deleteContent(@PathVariable("contentId") Long contentId) {
        contentService.deleteContent(contentId);
        return ResponseEntity.ok().build();
    }
}
