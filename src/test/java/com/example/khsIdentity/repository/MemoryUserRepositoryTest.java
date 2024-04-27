package com.example.khsIdentity.repository;

import com.example.khsIdentity.domain.User;
import com.example.khsIdentity.repository.User.MemoryUserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemoryUserRepositoryTest {

    MemoryUserRepository repository = new MemoryUserRepository();

    @AfterEach
    void afterEach() {
        repository.clearStore();
    }

    @Test
    void save(){
        User user = new User("spring00", "spring00", "spring00", "spring00@naver.com");

        repository.save(user);

        User result = repository.findById(user.getId()).get();

        assertThat(user).isEqualTo(result);
    }


    @Test
    void 이름_비었을_때() {
        User user = new User("", "spring00", "spring00", "spring00@naver.com");
        ConstraintViolationException e = assertThrows(ConstraintViolationException.class, () -> {
            repository.save(user);
        });

        assertThat(e.getMessage()).contains("이름을 작성해주세요!");
    }

    @Test
    void 아이디_비었을_때() {
        User user = new User("spring00", "", "spring00", "spring00@naver.com");
        ConstraintViolationException e = assertThrows(ConstraintViolationException.class, () -> {
            repository.save(user);
        });
        assertThat(e.getMessage()).contains("아이디를 작성해주세요!");
    }

    @Test
    void 비밀번호_유효성_검사() {
        User user = new User("spring00", "spring00", "00", "spring00@naver.com");
        ConstraintViolationException e = assertThrows(ConstraintViolationException.class, () -> {
            repository.save(user);
        });
        assertThat(e.getMessage()).contains("비밀번호를 최소 8자 작성해주세요!");
    }

    @Test
    void 이메일_유효성_검사() {
        User user = new User("spring00", "spring00", "spring00", "spring00naver.com");
        ConstraintViolationException e = assertThrows(ConstraintViolationException.class, () -> {
            repository.save(user);
        });
        assertThat(e.getMessage()).contains("이메일 형식에 맞게 작성해주세요!");
    }

    @Test
    void findByName(){
        User user1 = new User("spring11", "spring11", "spring11", "spring11@naver.com");
        repository.save(user1);

        User user2 = new User("spring22", "spring22", "spring22", "spring22@naver.com");
        repository.save(user2);

        List<User> results = repository.findByName("spring11");

        assertThat(results.get(1)).isEqualTo(user2);
    }

    @Test
    void findByUserId(){
        User user1 = new User("spring11", "spring11", "spring11", "spring11@naver.com");
        repository.save(user1);

        User user2 = new User("spring22", "spring22", "spring22", "spring22@naver.com");
        repository.save(user2);

        User result = repository.findByUserId("spring11").get();

        assertThat(result).isEqualTo(user1);
    }

    @Test
    void findByEmail(){
        User user1 = new User("spring11", "spring11", "spring11", "spring11@naver.com");
        repository.save(user1);

        User user2 = new User("spring22", "spring22", "spring22", "spring22@naver.com");
        repository.save(user2);

        User result = repository.findByEmail("spring11@naver.com").get();

        assertThat(result).isEqualTo(user1);
    }


    @Test
    void findAll(){
        User user1 = new User("spring11", "spring11", "spring11", "spring11@naver.com");
        repository.save(user1);

        User user2 = new User("spring22", "spring22", "spring22", "spring22@naver.com");
        repository.save(user2);

        List<User> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
