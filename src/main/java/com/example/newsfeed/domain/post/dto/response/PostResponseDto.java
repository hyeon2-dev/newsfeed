package com.example.newsfeed.domain.post.dto.response;

import com.example.newsfeed.domain.post.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private final Long id;
    private final Long userId;
    private final String contents;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public PostResponseDto(Long id, Long userId, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.contents = contents;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static PostResponseDto toDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getUser().getId(),
                post.getContents(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }

}
