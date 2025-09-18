package com.example.mediaboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostRequestDto {
    
    @NotBlank(message = "카테고리는 필수입니다")
    private String category;
    
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    private String content;
    
    @NotBlank(message = "작성자는 필수입니다")
    private String author;
    
    // 기본 생성자
    public PostRequestDto() {}
    
    // 생성자
    public PostRequestDto(String category, String title, String content, String author) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
    }
    
    // Getter and Setter methods
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
}
