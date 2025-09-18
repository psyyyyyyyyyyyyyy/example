package com.example.mediaboard.service;

import com.example.mediaboard.dto.PostRequestDto;
import com.example.mediaboard.dto.PostResponseDto;
import com.example.mediaboard.entity.Post;
import com.example.mediaboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    // 모든 게시글 조회
    public List<PostResponseDto> getAllPosts(String userIp) {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(post -> new PostResponseDto(post, userIp))
                .collect(Collectors.toList());
    }
    
    // 카테고리별 게시글 조회
    public List<PostResponseDto> getPostsByCategory(String category, String userIp) {
        List<Post> posts;
        if ("전체".equals(category)) {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        } else {
            posts = postRepository.findByCategoryOrderByCreatedAtDesc(category);
        }
        return posts.stream()
                .map(post -> new PostResponseDto(post, userIp))
                .collect(Collectors.toList());
    }
    
    // 카테고리와 미디어 타입별 게시글 조회
    public List<PostResponseDto> getPostsByCategoryAndMediaType(String category, String mediaType, String userIp) {
        List<Post> posts;
        
        if ("전체".equals(category)) {
            if ("전체".equals(mediaType)) {
                posts = postRepository.findAllByOrderByCreatedAtDesc();
            } else if ("사진".equals(mediaType)) {
                posts = postRepository.findPostsByMediaType("image");
            } else if ("동영상".equals(mediaType)) {
                posts = postRepository.findPostsByMediaType("video");
            } else {
                posts = postRepository.findAllByOrderByCreatedAtDesc();
            }
        } else {
            if ("전체".equals(mediaType)) {
                posts = postRepository.findByCategoryOrderByCreatedAtDesc(category);
            } else if ("사진".equals(mediaType)) {
                posts = postRepository.findPostsByCategoryAndMediaType(category, "image");
            } else if ("동영상".equals(mediaType)) {
                posts = postRepository.findPostsByCategoryAndMediaType(category, "video");
            } else {
                posts = postRepository.findByCategoryOrderByCreatedAtDesc(category);
            }
        }
        
        return posts.stream()
                .map(post -> new PostResponseDto(post, userIp))
                .collect(Collectors.toList());
    }
    
    // 게시글 상세 조회 (조회수 증가)
    public PostResponseDto getPostById(Long id, String userIp) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
        
        // 조회수 증가
        post.incrementViews();
        postRepository.save(post);
        
        return new PostResponseDto(post, userIp);
    }
    
    // 게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, String userIp) {
        Post post = new Post(
            requestDto.getCategory(),
            requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getAuthor()
        );
        
        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost, userIp);
    }
    
    // 게시글 수정
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, String userIp) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
        
        post.setCategory(requestDto.getCategory());
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        
        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost, userIp);
    }
    
    // 게시글 삭제
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
        
        postRepository.delete(post);
    }
    
    // 좋아요 토글
    public PostResponseDto toggleLike(Long id, String userIp) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
        
        boolean liked = post.toggleLike(userIp);
        Post savedPost = postRepository.save(post);
        
        return new PostResponseDto(savedPost, userIp);
    }
    
    // 제목으로 검색
    public List<PostResponseDto> searchPostsByTitle(String title, String userIp) {
        List<Post> posts = postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);
        return posts.stream()
                .map(post -> new PostResponseDto(post, userIp))
                .collect(Collectors.toList());
    }
    
    // 인기 게시글 (조회수 기준)
    public List<PostResponseDto> getPopularPostsByViews(String userIp) {
        List<Post> posts = postRepository.findTop10ByOrderByViewsDesc();
        return posts.stream()
                .map(post -> new PostResponseDto(post, userIp))
                .collect(Collectors.toList());
    }
    
    // 인기 게시글 (좋아요 기준)
    public List<PostResponseDto> getPopularPostsByLikes(String userIp) {
        List<Post> posts = postRepository.findTop10ByOrderByLikesDesc();
        return posts.stream()
                .map(post -> new PostResponseDto(post, userIp))
                .collect(Collectors.toList());
    }
}
