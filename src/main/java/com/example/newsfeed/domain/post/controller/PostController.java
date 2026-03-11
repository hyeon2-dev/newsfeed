package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.domain.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.domain.post.dto.response.PostResponseDto;
import com.example.newsfeed.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시물 생성
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> savePost(
            @Auth AuthUser authUser,
            @RequestBody PostSaveRequestDto dto
    ) {
        return ResponseEntity.ok(postService.savePost(authUser, dto));
    }

    // 본인 게시물 전체 조회(페이징)
    @GetMapping("/posts/page")
    public ResponseEntity<Page<PostResponseDto>> getMyPosts(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(postService.getMyPosts(authUser, page, size));
    }

    // 게시물 단일 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // 본인 게시물 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> updateMyPost(
            @Auth AuthUser authUser,
            @RequestBody PostUpdateRequestDto dto,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.updateMyPost(authUser, dto, postId));
    }

    // 본인 게시물 삭제
    @DeleteMapping("/posts/{postId}/delete")
    public ResponseEntity<String> deletePost(
            @Auth AuthUser authUser,
            @PathVariable Long postId
    ) {
        postService.deletePost(authUser, postId);
        return new ResponseEntity<>("게시물이 삭제되었습니다.", HttpStatus.OK);
    }
}
