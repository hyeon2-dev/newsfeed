package com.example.newsfeed.domain.comment.service;

import com.example.newsfeed.common.exception.BaseException;
import com.example.newsfeed.common.exception.ErrorCode;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.comment.dto.request.CommentSaveRequestDto;
import com.example.newsfeed.domain.comment.dto.request.CommentUpdateRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.post.dto.response.PostResponseDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 생성 (본인만)
    @Transactional
    public CommentResponseDto createComment(AuthUser authUser, Long postId, CommentSaveRequestDto dto) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BaseException(ErrorCode.POST_NOT_FOUND, null)
        );

        Comment comment = new Comment(user, post, dto.getContents());
        commentRepository.save(comment);

        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getContents(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }

    // 댓글 전체 조회(페이징)
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getAllComments(Long postId, int page, int size) {

        int adjustPage = (page > 0) ? page - 1 : 0;

        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("createdAt").descending());

        return commentRepository.findAllByPostId(postId, pageable)
                .map(CommentResponseDto::toDto);
    }

    // 댓글 수정 (본인만)
    @Transactional
    public CommentResponseDto updateComment(AuthUser authUser, Long postId, Long commentId, CommentUpdateRequestDto dto) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(
                () -> new BaseException(ErrorCode.COMMENT_NOT_FOUND, null)
        );

        if (!comment.getUser().getId().equals(authUser.getUserId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_COMMENT, null);
        }

        comment.updateContent(dto.getContents());

        return CommentResponseDto.toDto(comment);
    }

    // 댓글 삭제 (본인만)
    public void deleteComment(AuthUser authUser, Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(
                () -> new BaseException(ErrorCode.COMMENT_NOT_FOUND, null)
        );

        if (comment.getUser().getId().equals(authUser.getUserId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_COMMENT, null);
        }

        commentRepository.delete(comment);
    }
}
