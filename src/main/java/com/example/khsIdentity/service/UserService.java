package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.repository.User.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    *  회원가입
    * */
    public Long join(User user) {

        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            return userRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("User not found with userId : " + username));
        }
        throw new IllegalStateException("User not found in session.");
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByUserId(user.getUserId())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
                });
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                });
    }

    public List<User> findUsers() {return userRepository.findAll();}
    public Optional<User> findOne(Long id) {return userRepository.findById(id);}
    public Optional<User> findOneByUserId(String userId) {return userRepository.findByUserId(userId);}
    public Optional<User> findOneByEmail(String email) {return userRepository.findByEmail(email);}
}
