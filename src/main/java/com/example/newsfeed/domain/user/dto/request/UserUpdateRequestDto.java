package com.example.newsfeed.domain.user.dto.request;

import com.example.newsfeed.domain.user.enums.Mbti;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

    private String userName;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,7}$",
            message = "이메일 양식대로 입력해주세요.")
    private String email;

    private String info;

    private Mbti mbti;

}
