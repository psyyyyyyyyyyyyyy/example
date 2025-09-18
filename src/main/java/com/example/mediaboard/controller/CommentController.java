package com.example.mediaboard.controller;

import com.example.mediaboard.dto.CommentDto;
import com.example.mediaboard.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "댓글 API", description = "댓글 관련 API (작성, 조회, 수정, 삭제)")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    // 특정 게시글의 댓글 목록 조회
    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", 
                    content = @Content(schema = @Schema(implementation = CommentDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId) {
        try {
            List<CommentDto> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 댓글 작성
    @Operation(summary = "댓글 작성", description = "특정 게시글에 새로운 댓글을 작성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "댓글 작성 성공", 
                    content = @Content(schema = @Schema(implementation = CommentDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "댓글 데이터", required = true) @Valid @RequestBody CommentDto commentDto) {
        try {
            // 작성자가 없으면 기본값 설정
            if (commentDto.getAuthor() == null || commentDto.getAuthor().trim().isEmpty()) {
                commentDto.setAuthor("익명");
            }
            
            CommentDto createdComment = commentService.createComment(postId, commentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDto commentDto) {
        try {
            CommentDto updatedComment = commentService.updateComment(commentId, commentDto);
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 특정 게시글의 댓글 수 조회
    @GetMapping("/posts/{postId}/comments/count")
    public ResponseEntity<Long> getCommentCountByPostId(@PathVariable Long postId) {
        try {
            long commentCount = commentService.getCommentCountByPostId(postId);
            return ResponseEntity.ok(commentCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 특정 작성자의 댓글 목록
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByAuthor(@RequestParam String author) {
        try {
            List<CommentDto> comments = commentService.getCommentsByAuthor(author);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
