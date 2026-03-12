package com.example.newsfeed.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteAccountRequestDto {

    @NotBlank
    private String passwordCheck;

}
