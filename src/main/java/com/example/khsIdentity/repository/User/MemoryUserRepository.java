package com.example.khsIdentity.repository.User;

import com.example.khsIdentity.domain.User;
import jakarta.validation.*;

import java.util.*;
import java.util.stream.Collectors;

/*
* @ 테스트, 프로토타입 용 (실제 기능 X)
*
* 데이터베이스 연결을 설정하는
* 오버헤드 없이 애플리케이션의 기능을 확인
*
* 디자인과 기능을 실험할 때
* 인메모리 저장소를 사용하면
* 변경을 단순화하고 개발 속도를 높임
* */
public class MemoryUserRepository implements UserRepository {

    private static Map<Long, User> store = new HashMap<>();
    private static long sequence = 0L;
    private Validator validator;

    public MemoryUserRepository() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Override
    public User save(User user) {
        user.setId(++sequence);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        store.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return store.values().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findAny();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<User> findByName(String name) {
        return store.values().stream()
                .filter(user -> user.getName().equals(name)).collect(Collectors.toList());
    }

    public void clearStore() {
        store.clear();
    }
}
