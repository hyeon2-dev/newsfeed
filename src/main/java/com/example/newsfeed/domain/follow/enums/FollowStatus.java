package com.example.newsfeed.domain.follow.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowStatus {

    NOT_FOLLOWING(false),
    FOLLOWING(true);

    private final boolean status;
}
