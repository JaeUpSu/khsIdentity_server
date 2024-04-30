package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.repository.Content.ContentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ContentService {

    private ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Content updateBody(Long contentId, String newBody) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + contentId));

        content.setBody(newBody);
        return contentRepository.save(content);
    }

    public Content updateImage(Long contentId, byte[] newImage) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + contentId));

        content.setImage(newImage);
        return contentRepository.save(content);
    }

    @Transactional(readOnly = true)
    public Optional<Content> findOne(Long contentId) {
        return contentRepository.findById(contentId);
    }

    @Transactional(readOnly = true)
    public List<Content> findAll() {
        return contentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Content> findAllContentByFeed(Feed feed) {
        return contentRepository.findByFeed(feed);
    }

    @Transactional(readOnly = true)
    public Long getFeedIdByContentId(Long contentId) {
        Optional<Content> content = contentRepository.findById(contentId);
        return content.map(Content::getFeed)
                .map(Feed::getId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + contentId));
    }

    @Transactional(readOnly = true)
    public String getFeedTitleByContentId(Long contentId) {
        Optional<Content> content = contentRepository.findById(contentId);
        return content.map(Content::getFeed)
                .map(Feed::getTitle)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + contentId));
    }

    public void deleteContent(Long contentId) {
        contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Feed not found with id: " + contentId));

        contentRepository.deleteById(contentId);
    }
}
