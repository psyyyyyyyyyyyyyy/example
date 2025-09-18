package com.example.mediaboard.repository;

import com.example.mediaboard.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // 카테고리별 게시글 조회
    Page<Post> findByCategory(String category, Pageable pageable);
    
    // 카테고리별 게시글 조회 (리스트)
    List<Post> findByCategoryOrderByCreatedAtDesc(String category);
    
    // 전체 게시글 조회 (최신순)
    List<Post> findAllByOrderByCreatedAtDesc();
    
    // 제목으로 검색
    List<Post> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    
    // 카테고리와 제목으로 검색
    List<Post> findByCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(String category, String title);
    
    // 특정 미디어 타입을 가진 게시글 조회 (이미지만)
    @Query("SELECT DISTINCT p FROM Post p JOIN p.files f WHERE f.type LIKE :mediaType% ORDER BY p.createdAt DESC")
    List<Post> findPostsByMediaType(@Param("mediaType") String mediaType);
    
    // 카테고리와 미디어 타입으로 게시글 조회
    @Query("SELECT DISTINCT p FROM Post p JOIN p.files f WHERE p.category = :category AND f.type LIKE :mediaType% ORDER BY p.createdAt DESC")
    List<Post> findPostsByCategoryAndMediaType(@Param("category") String category, @Param("mediaType") String mediaType);
    
    // 조회수 상위 게시글
    List<Post> findTop10ByOrderByViewsDesc();
    
    // 좋아요 상위 게시글
    List<Post> findTop10ByOrderByLikesDesc();
}
