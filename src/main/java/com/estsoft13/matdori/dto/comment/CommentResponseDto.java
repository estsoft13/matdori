package com.estsoft13.matdori.dto.comment;

import com.estsoft13.matdori.domain.Comment;
import com.estsoft13.matdori.util.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class CommentResponseDto {
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private Long id;
    private Long userId;
    private Role role;
    private Boolean isCommentOwner;

//    public CommentResponseDto(Comment comment) {
//        this.content = comment.getContent();
//        this.createdAt = comment.getCreatedAt();
//    }

    public CommentResponseDto(
            Comment comment) {
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.username = comment.getUser().getEnteredUsername();
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.role = comment.getUser().getRole();
    }

    public CommentResponseDto(
            CommentResponseDto commentResponseDto, Boolean isCommentOwner) {
        this.content = commentResponseDto.getContent();
        this.createdAt = commentResponseDto.getCreatedAt();
        this.username = commentResponseDto.getUsername();
        this.id = commentResponseDto.getId();
        this.userId = commentResponseDto.getUserId();
        this.role = commentResponseDto.getRole();
        this.isCommentOwner = isCommentOwner;
    }

    @Builder
    public CommentResponseDto(String content, LocalDateTime createdAt, String username, Long id, Role role) {
        this.content=content;
        this.createdAt = createdAt;
        this.username= username;
        this.id=id;
        this.role=role;
    }

}

