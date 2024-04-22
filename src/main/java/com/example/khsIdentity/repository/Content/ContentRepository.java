package com.example.khsIdentity.repository.Content;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;

import java.util.List;
import java.util.Optional;

public interface ContentRepository {
    Content save(Content content);

    Optional<Content> findById(Long id);

    List<Content> findByFeed(Feed feed);

    List<Content> findAll();

    void delete(Content content);
}
