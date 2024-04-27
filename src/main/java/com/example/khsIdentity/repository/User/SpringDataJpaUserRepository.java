package com.example.khsIdentity.repository.User;

import com.example.khsIdentity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaUserRepository extends JpaRepository<User, Long>, UserRepository {
}
