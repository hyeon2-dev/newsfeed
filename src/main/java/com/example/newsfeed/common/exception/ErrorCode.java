package com.example.newsfeed.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 유저 관련 코드
    DUPLICATE_EMAIL("중복된 이메일이 있습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("비밀번호가 맞지 않습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("id에 맞는 유저가 없습니다.", HttpStatus.NOT_FOUND),
    SAME_AS_OLD_PASSWORD("현재 비밀번호와 동일합니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_CONFIRMATION_MISMATCH("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 게시물 관련 코드
    POST_NOT_FOUND("id에 맞는 게시물이 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN_POST("본인 게시물 아니어서 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 팔로우 관련 코드
    CANNOT_FOLLOW_SELF("본인이 본인을 팔로우할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_UNFOLLOW_SELF("본인이 본인을 언팔로우할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_FOLLOWING("이미 팔로우 상태입니다.", HttpStatus.CONFLICT),
    ALREADY_UNFOLLOWING("이미 언팔로우 상태입니다.", HttpStatus.CONFLICT),
    FOLLOW_NOT_FOUND("팔로우상태를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 댓글 관련 코드
    COMMENT_NOT_FOUND("id에 맞는 댓글이 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN_COMMENT("본인 댓글이 아니어서 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 게시물 좋아요 코드
    ALREADY_LIKED_POST("이미 좋아요를 눌렀습니다.", HttpStatus.CONFLICT),
    LIKE_NOT_FOUND("좋아요가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ALREADY_NOT_LIKE("이미 좋아요를 취소했습니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
