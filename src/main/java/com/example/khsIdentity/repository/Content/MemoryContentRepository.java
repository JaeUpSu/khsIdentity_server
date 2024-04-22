package com.example.khsIdentity.repository.Content;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;

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

public class MemoryContentRepository implements ContentRepository {

    private static Map<Long, Content> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Content save(Content content) {
        content.setId(++sequence);
        store.put(content.getId(), content);
        return content;
    }

    @Override
    public Optional<Content> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Content> findByFeed(Feed feed) {
        return store.values().stream()
                .filter(content -> content.getFeed() != null && content.getFeed().getId().equals(feed.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Content> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(Content content) {
        if (content != null && content.getId() != null) {
            store.remove(content.getId());
        }
    }

    public void clearStore() {
        store.clear();
    }
}
