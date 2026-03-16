package com.example.newsfeed.domain.comment.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.comment.dto.request.CommentSaveRequestDto;
import com.example.newsfeed.domain.comment.dto.request.CommentUpdateRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성 (본인만)
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment (
            @Auth AuthUser authUser,
            @PathVariable Long postId,
            @RequestBody CommentSaveRequestDto dto
    ) {
        return ResponseEntity.ok(commentService.createComment(authUser, postId, dto));
    }

    // 댓글 전체 조회(페이징)
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getAllComments(
        @PathVariable Long postId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getAllComments(postId, page, size));
    }

    // 댓글 수정 (본인만)
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @Auth AuthUser authUser,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(commentService.updateComment(authUser, postId, commentId, dto));
    }

    // 댓글 삭제 (본인만)
    @DeleteMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @Auth AuthUser authUser,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(authUser, postId, commentId);
        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }


}
