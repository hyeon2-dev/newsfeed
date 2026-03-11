package com.example.newsfeed.domain.post.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post(String contents, User user) {
        this.contents = contents;
        this.user = user;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }
}
