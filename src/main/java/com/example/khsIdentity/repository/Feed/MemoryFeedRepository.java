package com.example.khsIdentity.repository.Feed;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.domain.User;

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
public class MemoryFeedRepository implements FeedRepository {

    private static Map<Long, Feed> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Feed save(Feed feed) {
        feed.setId(++sequence);
        store.put(feed.getId(), feed);
        return feed;
    }

    @Override
    public Optional<Feed> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Feed> findByTitleContainingIgnoreCase(String title) {
        return store.values().stream()
                .filter(feed -> feed.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Feed> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(store.get(id).getUser());
    }

    public List<Feed> findAllByUser_UserId(String userId) {
        return store.values().stream()
                .filter(feed -> feed.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public void clearStore() {
        store.clear();
    }
}
