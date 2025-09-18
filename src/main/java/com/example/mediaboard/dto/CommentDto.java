package com.example.mediaboard.dto;

import com.example.mediaboard.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class CommentDto {
    
    private Long id;
    private String author;
    private String content;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    // 기본 생성자
    public CommentDto() {}
    
    // Entity에서 DTO로 변환하는 생성자
    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getAuthor();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
    
    // 생성자
    public CommentDto(String author, String content) {
        this.author = author;
        this.content = content;
    }
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
