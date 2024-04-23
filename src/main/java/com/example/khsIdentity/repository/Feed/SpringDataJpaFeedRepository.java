package com.example.khsIdentity.repository.Feed;

import com.example.khsIdentity.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaFeedRepository extends JpaRepository<Feed, Long>, FeedRepository {
    List<Feed> findAllByUser_UserId(String userId);
}