package com.example.newsfeed.domain.auth.dto.response;

import com.example.newsfeed.domain.user.enums.Mbti;
import lombok.Getter;

@Getter
public class SignupResponseDto {

    private final Long id;
    private final String userName;
    private final String email;
    private final String password;
    private final String info;
    private final Mbti mbti;

    public SignupResponseDto(Long id, String userName, String email, String password, String info, Mbti mbti) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.info = info;
        this.mbti = mbti;
    }
}
