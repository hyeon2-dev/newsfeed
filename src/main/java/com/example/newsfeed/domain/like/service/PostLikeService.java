package com.example.newsfeed.domain.like.service;

import com.example.newsfeed.common.exception.BaseException;
import com.example.newsfeed.common.exception.ErrorCode;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.like.entity.PostLike;
import com.example.newsfeed.domain.like.enums.LikeStatus;
import com.example.newsfeed.domain.like.repository.PostLikeRepository;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 게시물 좋아요
    @Transactional
    public void postLike(AuthUser authUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BaseException(ErrorCode.POST_NOT_FOUND, null)
        );

        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        Optional<PostLike> optionalPostLike = postLikeRepository.findByUserIdAndPostId(user.getId(), postId);

        if (optionalPostLike.isPresent()) {

            PostLike postLike = optionalPostLike.get();

            if(postLike.getLikeStatus() == LikeStatus.LIKE) {
                throw new BaseException(ErrorCode.ALREADY_LIKED_POST, null);
            }

            postLike.updateStatus(LikeStatus.LIKE);
        } else {
            PostLike postLike = new PostLike(user, post, LikeStatus.LIKE);
            postLikeRepository.save(postLike);
        }

        post.increaseLikeCount();
    }

    // 게시물 좋아요 삭제
    @Transactional
    public void postUnlike(AuthUser authUser, Long postId) {
        PostLike postLike = postLikeRepository.findByUserIdAndPostId(authUser.getUserId(), postId).orElseThrow(
                () -> new BaseException(ErrorCode.LIKE_NOT_FOUND, null)
        );

        if (postLike.getLikeStatus() == LikeStatus.NOT_LIKE) {
            throw new BaseException(ErrorCode.ALREADY_NOT_LIKE, null);
        }

        postLike.cancelLike(LikeStatus.NOT_LIKE);

        postLike.getPost().decreaseLikeCount();
    }
}
