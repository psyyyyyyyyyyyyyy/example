package com.example.mediaboard.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private String author;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(columnDefinition = "integer default 0")
    private Integer likes = 0;
    
    @Column(columnDefinition = "integer default 0")
    private Integer views = 0;
    
    @Column(columnDefinition = "integer default 0")
    private Integer comments = 0;
    
    // 좋아요한 사용자 IP 목록 (간단한 구현)
    @ElementCollection
    @CollectionTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "user_ip")
    private List<String> likedUserIps = new ArrayList<>();
    
    // 미디어 파일들
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MediaFile> files = new ArrayList<>();
    
    // 댓글들
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();
    
    // 기본 생성자
    public Post() {}
    
    // 생성자
    public Post(String category, String title, String content, String author) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
    
    public List<String> getLikedUserIps() {
        return likedUserIps;
    }
    
    public void setLikedUserIps(List<String> likedUserIps) {
        this.likedUserIps = likedUserIps;
    }
    
    public List<MediaFile> getFiles() {
        return files;
    }
    
    public void setFiles(List<MediaFile> files) {
        this.files = files;
    }
    
    public List<Comment> getCommentList() {
        return commentList;
    }
    
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
    
    // 헬퍼 메서드들
    public void incrementViews() {
        this.views++;
    }
    
    public void incrementComments() {
        this.comments++;
    }
    
    public void decrementComments() {
        if (this.comments > 0) {
            this.comments--;
        }
    }
    
    public boolean toggleLike(String userIp) {
        if (likedUserIps.contains(userIp)) {
            likedUserIps.remove(userIp);
            likes--;
            return false; // 좋아요 취소
        } else {
            likedUserIps.add(userIp);
            likes++;
            return true; // 좋아요 추가
        }
    }
    
    public boolean isLikedBy(String userIp) {
        return likedUserIps.contains(userIp);
    }
}
