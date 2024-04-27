package com.example.khsIdentity.service;

import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.dto.UserDTO;
import com.example.khsIdentity.repository.User.MemoryUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {

    UserService userService;
    MemoryUserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        userRepository = new MemoryUserRepository();
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @AfterEach
    void afterEach() {userRepository.clearStore();}

    @Test
    void 회원가입() {
        // given (이런 상황이 주어지면)
        User user = new User("spring00", "spring00", "spring00", "spring00@naver.com");

        // when (이거를 실행 했을 때)
        UserDTO user1 = userService.join(user);

        // then (이 결과가 기대돼)
        User findMember = userService.findOneByUserId(user1.getUserId()).get();
        assertThat(user.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복_아이디_예외() {
        // given
        User user1 = new User("spring11", "spring11", "spring11", "spring11@naver.com");
        User user2 = new User("spring22", "spring11", "spring22", "spring22@naver.com");

        // when
        userService.join(user1);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> userService.join(user2));

        // then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 아이디입니다.");
    }

    @Test
    void 중복_이메일_예외() {
        // given
        User user1 = new User("spring11", "spring11", "spring11", "spring11@naver.com");
        User user2 = new User("spring22", "spring22", "spring22", "spring11@naver.com");

        // when
        userService.join(user1);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> userService.join(user2));

        // then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }
}
