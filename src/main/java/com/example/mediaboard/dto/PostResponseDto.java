package com.example.mediaboard.dto;

import com.example.mediaboard.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostResponseDto {
    
    private Long id;
    private String category;
    private String title;
    private String content;
    private String author;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    private Integer likes;
    private Integer views;
    private Integer comments;
    private Boolean isLiked;
    private List<MediaFileDto> files;
    private List<CommentDto> commentList;
    
    // 기본 생성자
    public PostResponseDto() {}
    
    // Entity에서 DTO로 변환하는 생성자
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();
        this.likes = post.getLikes();
        this.views = post.getViews();
        this.comments = post.getComments();
        this.isLiked = false; // 기본값, 나중에 설정
        this.files = post.getFiles().stream()
                .map(MediaFileDto::new)
                .collect(Collectors.toList());
        this.commentList = post.getCommentList().stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }
    
    // Entity에서 DTO로 변환 (좋아요 상태 포함)
    public PostResponseDto(Post post, String userIp) {
        this(post);
        this.isLiked = post.isLikedBy(userIp);
    }
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Integer getLikes() {
        return likes;
    }
    
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    
    public Integer getViews() {
        return views;
    }
    
    public void setViews(Integer views) {
        this.views = views;
    }
    
    public Integer getComments() {
        return comments;
    }
    
    public void setComments(Integer comments) {
        this.comments = comments;
    }
    
    public Boolean getIsLiked() {
        return isLiked;
    }
    
    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }
    
    public List<MediaFileDto> getFiles() {
        return files;
    }
    
    public void setFiles(List<MediaFileDto> files) {
        this.files = files;
    }
    
    public List<CommentDto> getCommentList() {
        return commentList;
    }
    
    public void setCommentList(List<CommentDto> commentList) {
        this.commentList = commentList;
    }
}
