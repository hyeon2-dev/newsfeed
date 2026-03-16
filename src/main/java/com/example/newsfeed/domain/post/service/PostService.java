package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.common.exception.BaseException;
import com.example.newsfeed.common.exception.ErrorCode;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.domain.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.domain.post.dto.response.PostResponseDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시물 생성
    @Transactional
    public PostResponseDto savePost(AuthUser authUser, PostSaveRequestDto dto) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        Post post = new Post(dto.getContents(), user);
        postRepository.save(post);

        return new PostResponseDto(
                post.getId(),
                post.getUser().getId(),
                post.getContents(),
                post.getLikeCount(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }

    // 게시물 전체 조회(페이징)
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getMyPosts(AuthUser authUser, int page, int size) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        // Spring Page는 0부터 시작하기 때문에
        // 클라이언트가 1부터 요청하면 1을 빼서 보정
        int adjustPage = (page > 0) ? page - 1 : 0;

        // 페이지 번호, 페이지 크기, 정렬 조건을 포함한 Pageable 생성
        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("createdAt").descending());

        // 로그인한 사용자의 게시글 최신순으로 페이징 조회한 뒤 DTO로 변환하여 반환
        return postRepository.findAllByUser(user, pageable)
                .map(PostResponseDto::toDto);
    }

    // 게시물 단일 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BaseException(ErrorCode.POST_NOT_FOUND, null)
        );

        return PostResponseDto.toDto(post);
    }

    // 본인 게시물 수정
    @Transactional
    public PostResponseDto updateMyPost(AuthUser authUser, PostUpdateRequestDto dto, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BaseException(ErrorCode.POST_NOT_FOUND, null)
        );

        if (!authUser.getUserId().equals(post.getUser().getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_POST, null);
        }

        post.updateContents(dto.getContents());

        return PostResponseDto.toDto(post);
    }

    // 본인 게시물 삭제
    public void deletePost(AuthUser authUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BaseException(ErrorCode.POST_NOT_FOUND, null)
        );

        if (!authUser.getUserId().equals(post.getUser().getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN_POST, null);
        }

        postRepository.delete(post);
    }








/* 예전 코드 위에 사용하는 코드는 새로운 방식으로 짜봄
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getMyPosts(AuthUser authUser, int page, int size) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        // Spring Page는 0부터 시작하기 때문에
        // 클라이언트가 1부터 요청하면 1을 빼서 보정
        int adjustPage = (page > 0) ? page - 1 : 0;

        // 페이지 번호, 페이지 크기, 정렬 조건을 포함한 Pageable 생성
        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("createdAt").descending());

        // 게시글을 페이징으로 조회
        // Page 객체에는 게시글 리스트 + 전체 데이터 수 + 페이지 정보가 포함됨
        Page<Post> postPage = postRepository.findAllByUser(user, pageable);

        // 조회한 게시글(Entity)를 DTO로 변환
        List<PostResponseDto> dtoList = postPage.getContent().stream()
                .map(PostResponseDto::toDto)
                .toList();

        // DTO 리스트를 Page 객체로 다시 감싸서 반환
        // pageable : 현재 페이지 정보
        // postPage.getTotalElements() : 전체 게시글 수
        return new PageImpl<>(dtoList, pageable, postPage.getTotalElements());
    }
 */
}
