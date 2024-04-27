package com.example.khsIdentity;

import com.example.khsIdentity.repository.Content.ContentRepository;
import com.example.khsIdentity.repository.Feed.FeedRepository;
import com.example.khsIdentity.repository.User.UserRepository;
import com.example.khsIdentity.service.ContentService;
import com.example.khsIdentity.service.FeedService;
import com.example.khsIdentity.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableJpaAuditing
public class SpringConfig {
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ContentRepository contentRepository;

    public SpringConfig(UserRepository userRepository, FeedRepository feedRepository, ContentRepository contentRepository) {
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
        this.contentRepository = contentRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository, passwordEncoder());
    }

    @Bean
    public FeedService feedService() {return new FeedService(feedRepository, userRepository);}

    @Bean
    public ContentService contentService() {return new ContentService(contentRepository);}
}