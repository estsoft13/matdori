package com.estsoft13.matdori.controller;

import com.estsoft13.matdori.dto.AddCommentRequestDto;
import com.estsoft13.matdori.dto.CommentResponseDto;
import com.estsoft13.matdori.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "CommentController/댓글 CRUD", description = "댓글 관리 API")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    @GetMapping("/api/comments/{reviewId}")
    @Operation(summary = "리뷰 상세페이지 댓글 모두 보기", description = "리뷰 상세페이지 댓글 모두 보기")
//    @ApiResponse(responseCode = "100", description = "요청 성공", content = @Content(mediaType = "application/json"))
    @Parameter(name = "id", description = "리뷰 상세페이지 ID", example = "1")
    public List<CommentResponseDto> getAllComments(@PathVariable Long reviewId) {
        return commentService.getAllComments(reviewId);
    }

    @PostMapping("/api/comment/{reviewId}")
    public CommentResponseDto createCommentToReview(@PathVariable Long reviewId ,@RequestBody AddCommentRequestDto requestDto) {
        return commentService.createCommentToReview(reviewId, requestDto);
    }

    @PutMapping("/api/comment/{reviewId}/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long reviewId,
                                                            @PathVariable Long commentId,
                                                            @RequestBody AddCommentRequestDto requestDto) {

        return ResponseEntity.ok(commentService.updateComment(reviewId, commentId, requestDto));
    }

    @DeleteMapping("api/comment/{reviewId}/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long reviewId,
                                              @PathVariable Long commentId) {
        commentService.deleteComment(reviewId, commentId);
        return ResponseEntity.ok().build();
    }

}
