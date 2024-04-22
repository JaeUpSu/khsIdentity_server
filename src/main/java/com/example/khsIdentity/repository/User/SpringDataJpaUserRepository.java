package com.example.khsIdentity.repository.User;

import com.example.khsIdentity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataJpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    @Query("select u from User u where u.name = :name")
    Optional<User> findByName(@Param("name") String name);

    @Query("select u from User u where u.userId = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);

    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
