package com.example.newsfeed.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentSaveRequestDto {

    @NotBlank(message = "댓글을 입력해주세요.")
    private String contents;

}
