package com.example.newsfeed.domain.follow.repository;

import com.example.newsfeed.domain.follow.dto.response.FollowResponseDto;
import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.follow.enums.FollowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerUserIdAndFollowingUserId(Long followerId, Long followingId);

    @Query("SELECT f FROM Follow f WHERE f.followerUser.id = :follower_id AND f.followStatus = :status")
    Page<Follow> findByFollowerUserId(
            @Param("follower_id") Long userId,
            @Param("status") FollowStatus status,
            Pageable pageable
    );

    @Query("SELECT f FROM Follow f WHERE f.followingUser.id = :following_id AND f.followStatus = :status")
    Page<Follow> findByFollowingUserId(
            @Param("following_id") Long userId,
            @Param("status") FollowStatus followStatus,
            Pageable pageable
    );
}
