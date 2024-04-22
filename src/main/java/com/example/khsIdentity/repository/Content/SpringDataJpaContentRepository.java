package com.example.khsIdentity.repository.Content;

import com.example.khsIdentity.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaContentRepository extends JpaRepository<Content, Long>, ContentRepository {
}