package com.example.newsfeed.domain.user.dto.response;

import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.enums.Mbti;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String userName;
    private final String email;
    private final String info;
    private final Mbti mbti;

    public UserResponseDto(Long id, String userName, String email, String info, Mbti mbti) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.info = info;
        this.mbti = mbti;
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getInfo(),
                user.getMbti()
        );
    }

}
