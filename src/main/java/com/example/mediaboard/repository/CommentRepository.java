package com.example.mediaboard.repository;

import com.example.mediaboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 특정 게시글의 댓글들 조회 (최신순)
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
    
    // 특정 게시글의 댓글들 조회 (오래된순)
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
    
    // 특정 작성자의 댓글들
    List<Comment> findByAuthorOrderByCreatedAtDesc(String author);
    
    // 특정 게시글의 댓글 수
    long countByPostId(Long postId);
}
