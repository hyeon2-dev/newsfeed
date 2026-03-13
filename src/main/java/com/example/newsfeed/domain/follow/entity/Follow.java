package com.example.newsfeed.domain.follow.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.domain.follow.enums.FollowStatus;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "follows",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"follower_id", "following_id"})
        }
)
public class Follow extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User followerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User followingUser;

    @Enumerated(EnumType.STRING)
    private FollowStatus followStatus;

    public Follow(User followerUser, User followingUser, FollowStatus followStatus) {
        this.followerUser = followerUser;
        this.followingUser = followingUser;
        this.followStatus = followStatus;
    }

    public boolean isFollowing() {
        return this.followStatus == FollowStatus.FOLLOWING;
    }

    public void updateFollowStatus(FollowStatus followStatus) {
        this.followStatus = followStatus;
    }
}
