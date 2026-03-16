package com.example.newsfeed.domain.like.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeStatus {

    LIKE(true),
    NOT_LIKE(false);

    private final boolean status;
}
