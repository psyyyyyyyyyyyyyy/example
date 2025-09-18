package com.example.mediaboard.dto;

import com.example.mediaboard.entity.MediaFile;

public class MediaFileDto {
    
    private Long id;
    private String type;
    private String name;
    private Long size;
    private String url;
    private String thumbnail;
    
    // 기본 생성자
    public MediaFileDto() {}
    
    // Entity에서 DTO로 변환하는 생성자
    public MediaFileDto(MediaFile mediaFile) {
        this.id = mediaFile.getId();
        this.type = mediaFile.getType();
        this.name = mediaFile.getName();
        this.size = mediaFile.getSize();
        this.url = mediaFile.getUrl();
        this.thumbnail = mediaFile.getThumbnail();
    }
    
    // 생성자
    public MediaFileDto(String type, String name, Long size, String url) {
        this.type = type;
        this.name = name;
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
}
