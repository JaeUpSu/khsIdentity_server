package com.example.khsIdentity.repository.Feed;

import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaFeedRepository extends JpaRepository<Feed, Long>, FeedRepository {
    @Query("select f from Feed f where f.id = :id")
    Optional<User> findUserById(@Param("id") Long id);

    List<Feed> findByTitleContainingIgnoreCase(@Param("title") String title);
}