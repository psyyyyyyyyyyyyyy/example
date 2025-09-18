package com.example.mediaboard.service;

import com.example.mediaboard.dto.CommentDto;
import com.example.mediaboard.entity.Comment;
import com.example.mediaboard.entity.Post;
import com.example.mediaboard.repository.CommentRepository;
import com.example.mediaboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    // 특정 게시글의 댓글 목록 조회
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }
    
    // 댓글 작성
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + postId));
        
        Comment comment = new Comment(post, commentDto.getAuthor(), commentDto.getContent());
        Comment savedComment = commentRepository.save(comment);
        
        // 게시글의 댓글 수 증가
        post.incrementComments();
        postRepository.save(post);
        
        return new CommentDto(savedComment);
    }
    
    // 댓글 삭제
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. ID: " + commentId));
        
        Post post = comment.getPost();
        commentRepository.delete(comment);
        
        // 게시글의 댓글 수 감소
        post.decrementComments();
        postRepository.save(post);
    }
    
    // 댓글 수정
    public CommentDto updateComment(Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. ID: " + commentId));
        
        comment.setContent(commentDto.getContent());
        Comment savedComment = commentRepository.save(comment);
        
        return new CommentDto(savedComment);
    }
    
    // 특정 게시글의 댓글 수 조회
    public long getCommentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
    
    // 특정 작성자의 댓글 목록
    public List<CommentDto> getCommentsByAuthor(String author) {
        List<Comment> comments = commentRepository.findByAuthorOrderByCreatedAtDesc(author);
        return comments.stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }
}
