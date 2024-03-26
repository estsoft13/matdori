package com.estsoft13.matdori.dto;

import com.estsoft13.matdori.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class AddCommentRequestDto {
    private String content;
    private LocalDateTime createdAt;

    public Comment toEntity() {
        return Comment.builder().content(content).createdAt(createdAt).build();
    }
}

