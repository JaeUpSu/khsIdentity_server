package com.example.khsIdentity.repository.Feed;

import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;

import java.util.List;
import java.util.Optional;

public interface FeedRepository {
    Feed save(Feed feed);
    Optional<Feed> findById(Long id);
    Optional<User> findUserById(Long id);
    List<Feed> findByTitleContainingIgnoreCase(String title);
    List<Feed> findAllByUserId(String userId);
    List<Feed> findAll();
    void delete(Long id);
}
