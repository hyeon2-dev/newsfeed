package com.example.newsfeed.domain.like.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.domain.like.enums.LikeStatus;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "postlikes")
public class PostLike extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private LikeStatus likeStatus;

    public PostLike(User user, Post post, LikeStatus likeStatus) {
        this.user = user;
        this.post = post;
        this.likeStatus = likeStatus;
    }

    public void cancelLike(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;
    }

    public void updateStatus(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;
    }
}
