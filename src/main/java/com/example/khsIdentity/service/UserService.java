package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.dto.UserDTO;
import com.example.khsIdentity.mapper.Mapper;
import com.example.khsIdentity.repository.User.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    *  회원가입
    * */
    public UserDTO join(User user) {
        validateDuplicateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User newUser = userRepository.save(user);
        return new Mapper().convertUserToDto(newUser);
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

    public UserDTO getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            return new Mapper().convertUserToDto(userRepository.findByUserId(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with userId : " + username)));
        }
        throw new IllegalStateException("User not found in session.");
    }

    public UserDTO login(String userId, String password) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId : " + userId));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new Mapper().convertUserToDto(user);
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }

    public Optional<User> findOne(Long id) {return userRepository.findById(id);}
    public Optional<User> findOneByUserId(String userId) {return userRepository.findByUserId(userId);}
    public Optional<User> findOneByEmail(String email) {return userRepository.findByEmail(email);}


    public List<User> findUsers() {
        return userRepository.findAll();
    }
}
