package com.example.newsfeed.domain.follow.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.follow.dto.response.FollowResponseDto;
import com.example.newsfeed.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/users/{userId}/follow")
    public ResponseEntity<String> follow (
            @Auth AuthUser authUser,
            @PathVariable Long userId
    ) {
        followService.follow(authUser, userId);
        return new ResponseEntity<>("팔로우 되었습니다.", HttpStatus.OK);
    }

    // 언팔로우
    @DeleteMapping("/users/{userId}/follow")
    public ResponseEntity<String> unfollow (
            @Auth AuthUser authUser,
            @PathVariable Long userId
    ) {
        followService.unfollow(authUser, userId);
        return new ResponseEntity<>("언팔로우 되었습니다.", HttpStatus.OK);
    }

    // 팔로잉(내가 팔로우를 인원) 목록 조회(페이징)
    @GetMapping("/users/me/followings")
    public ResponseEntity<Page<FollowResponseDto>> getAllFollowings(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(followService.getAllFollowings(authUser, page, size));
    }

    // 팔로워(나를 팔로우한 인원) 목록 조회(페이징)
    @GetMapping("/users/me/followers")
    public ResponseEntity<Page<FollowResponseDto>> getAllFollowers(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(followService.getAllFollowers(authUser, page, size));
    }

}
