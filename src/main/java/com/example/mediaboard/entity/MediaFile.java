package com.example.mediaboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "media_files")
public class MediaFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @Column(nullable = false)
    private String type; // MIME type (image/jpeg, video/mp4 등)
    
    @Column(nullable = false)
    private String name; // 원본 파일명
    
    @Column(nullable = false)
    private String fileName; // 서버에 저장된 파일명
    
    @Column(nullable = false)
    private String filePath; // 파일 경로
    
    @Column(nullable = false)
    private Long size; // 파일 크기 (bytes)
    
    private String url; // 접근 URL
    
    private String thumbnail; // 동영상 썸네일 URL (동영상인 경우)
    
    // 기본 생성자
    public MediaFile() {}
    
    // 생성자
    public MediaFile(Post post, String type, String name, String fileName, 
                    String filePath, Long size, String url) {
        this.post = post;
        this.type = type;
        this.name = name;
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.url = url;
    }
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Post getPost() {
        return post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getThumbnail() {
        return thumbnail;
    }
    
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    // 헬퍼 메서드들
    public boolean isImage() {
        return type != null && type.startsWith("image/");
    }
    
    public boolean isVideo() {
        return type != null && type.startsWith("video/");
    }
}
