package com.example.mediaboard.repository;

import com.example.mediaboard.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    
    // 특정 게시글의 파일들
    List<MediaFile> findByPostIdOrderById(Long postId);
    
    // 특정 타입의 파일들
    List<MediaFile> findByTypeStartingWithOrderByIdDesc(String typePrefix);
    
    // 특정 게시글의 이미지 파일들
    List<MediaFile> findByPostIdAndTypeStartingWithOrderById(Long postId, String typePrefix);
    
    // 파일명으로 검색
    MediaFile findByFileName(String fileName);
}
