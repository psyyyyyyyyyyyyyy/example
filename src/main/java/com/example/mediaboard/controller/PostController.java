package com.example.mediaboard.controller;

import com.example.mediaboard.dto.PostRequestDto;
import com.example.mediaboard.dto.PostResponseDto;
import com.example.mediaboard.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "게시글 API", description = "게시글 관련 API (생성, 조회, 수정, 삭제, 좋아요)")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    // 클라이언트 IP 주소 가져오기
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0];
        }
    }
    
    // 모든 게시글 조회 또는 필터링된 게시글 조회
    @Operation(summary = "게시글 목록 조회", description = "모든 게시글을 조회하거나 카테고리, 미디어 타입, 검색어로 필터링하여 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", 
                    content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts(
            @Parameter(description = "카테고리 필터 (전체, 풍경, 음식, 인물, 여행, 일상)") 
            @RequestParam(required = false) String category,
            @Parameter(description = "미디어 타입 필터 (전체, 사진, 동영상)") 
            @RequestParam(required = false) String mediaType,
            @Parameter(description = "제목 검색어") 
            @RequestParam(required = false) String search,
            HttpServletRequest request) {
        
        String userIp = getClientIpAddress(request);
        List<PostResponseDto> posts;
        
        try {
            if (search != null && !search.trim().isEmpty()) {
                // 검색 기능
                posts = postService.searchPostsByTitle(search, userIp);
            } else if (category != null && mediaType != null) {
                // 카테고리와 미디어 타입으로 필터링
                posts = postService.getPostsByCategoryAndMediaType(category, mediaType, userIp);
            } else if (category != null) {
                // 카테고리로 필터링
                posts = postService.getPostsByCategory(category, userIp);
            } else {
                // 모든 게시글
                posts = postService.getAllPosts(userIp);
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 특정 게시글 조회 (조회수 증가)
    @Operation(summary = "게시글 상세 조회", description = "특정 게시글을 조회합니다. 조회할 때마다 조회수가 1 증가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", 
                    content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long id, 
            HttpServletRequest request) {
        try {
            String userIp = getClientIpAddress(request);
            PostResponseDto post = postService.getPostById(id, userIp);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 게시글 생성
    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "게시글 작성 성공", 
                    content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @Parameter(description = "게시글 작성 데이터", required = true)
            @Valid @RequestBody PostRequestDto requestDto,
            HttpServletRequest request) {
        try {
            String userIp = getClientIpAddress(request);
            PostResponseDto createdPost = postService.createPost(requestDto, userIp);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDto requestDto,
            HttpServletRequest request) {
        try {
            String userIp = getClientIpAddress(request);
            PostResponseDto updatedPost = postService.updatePost(id, requestDto, userIp);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 좋아요 토글
    @Operation(summary = "좋아요 토글", description = "게시글의 좋아요를 추가하거나 제거합니다. 같은 IP에서 중복 좋아요는 불가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "좋아요 토글 성공"),
        @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long id, 
            HttpServletRequest request) {
        try {
            String userIp = getClientIpAddress(request);
            PostResponseDto post = postService.toggleLike(id, userIp);
            
            // 좋아요 상태와 개수 반환
            Map<String, Object> response = Map.of(
                "isLiked", post.getIsLiked(),
                "likes", post.getLikes()
            );
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 인기 게시글 조회 (조회수 기준)
    @GetMapping("/popular/views")
    public ResponseEntity<List<PostResponseDto>> getPopularPostsByViews(HttpServletRequest request) {
        try {
            String userIp = getClientIpAddress(request);
            List<PostResponseDto> posts = postService.getPopularPostsByViews(userIp);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 인기 게시글 조회 (좋아요 기준)
    @GetMapping("/popular/likes")
    public ResponseEntity<List<PostResponseDto>> getPopularPostsByLikes(HttpServletRequest request) {
        try {
            String userIp = getClientIpAddress(request);
            List<PostResponseDto> posts = postService.getPopularPostsByLikes(userIp);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
