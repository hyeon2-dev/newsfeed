package com.example.newsfeed.domain.follow.service;

import com.example.newsfeed.common.exception.BaseException;
import com.example.newsfeed.common.exception.ErrorCode;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.follow.dto.response.FollowResponseDto;
import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.follow.enums.FollowStatus;
import com.example.newsfeed.domain.follow.repository.FollowRepository;
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
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우
    @Transactional
    public void follow(AuthUser authUser, Long userId) {
        if (authUser.getUserId().equals(userId)) {
            throw new BaseException(ErrorCode.CANNOT_FOLLOW_SELF, null);
        }

        Follow follow = followRepository.findByFollowerUserIdAndFollowingUserId(authUser.getUserId(), userId).orElse(null);

        if(follow != null) {
            if(follow.isFollowing()) {
                throw new BaseException(ErrorCode.ALREADY_FOLLOWING, null);
            }

            follow.updateFollowStatus(FollowStatus.FOLLOWING);
            return;
        }

        User follower = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        User following = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        followRepository.save(new Follow(follower, following, FollowStatus.FOLLOWING));

    }

    // 언팔로우
    @Transactional
    public void unfollow(AuthUser authUser, Long userId) {
        if (authUser.getUserId().equals(userId)) {
            throw new BaseException(ErrorCode.CANNOT_UNFOLLOW_SELF, null);
        }

        Follow follow = followRepository.findByFollowerUserIdAndFollowingUserId(authUser.getUserId(), userId).orElseThrow(
                () -> new BaseException(ErrorCode.FOLLOW_NOT_FOUND, null)
        );

        if(!follow.isFollowing()) {
            throw new BaseException(ErrorCode.ALREADY_UNFOLLOWING, null);
        }

        follow.updateFollowStatus(FollowStatus.NOT_FOLLOWING);
    }

    // 팔로잉(내가 팔로우를 인원) 목록 조회(페이징)
    @Transactional
    public Page<FollowResponseDto> getAllFollowings(AuthUser authUser, int page, int size) {

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("modifiedAt").descending());

        return followRepository.findByFollowerUserId(authUser.getUserId(), FollowStatus.FOLLOWING, pageable)
                .map(FollowResponseDto::toDto);
    }

    // 팔로워(나를 팔로우한 인원) 목록 조회(페이징)
    @Transactional
    public Page<FollowResponseDto> getAllFollowers(AuthUser authUser, int page, int size) {

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("modifiedAt").descending());

        return followRepository.findByFollowingUserId(authUser.getUserId(), FollowStatus.FOLLOWING, pageable)
                .map(FollowResponseDto::toDto);
    }
}
