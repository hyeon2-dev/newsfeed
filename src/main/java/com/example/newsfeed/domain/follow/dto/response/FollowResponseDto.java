package com.example.newsfeed.domain.follow.dto.response;

import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.user.entity.User;
import lombok.Getter;

@Getter
public class FollowResponseDto {

    private final Long id;
    private final Long followingId;

    public FollowResponseDto(Long id, Long followingId) {
        this.id = id;
        this.followingId = followingId;
    }

    public static FollowResponseDto toDto(Follow follow) {
        return new FollowResponseDto(
                follow.getFollowerUser().getId(),
                follow.getFollowingUser().getId()
        );
    }
}
