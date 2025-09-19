package com.example.mediaboard.service;

import com.example.mediaboard.dto.MediaFileDto;
import com.example.mediaboard.entity.MediaFile;
import com.example.mediaboard.entity.Post;
import com.example.mediaboard.repository.MediaFileRepository;
import com.example.mediaboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileUploadService {
    
    @Autowired
    private MediaFileRepository mediaFileRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    // Cloudinary 폴더명
    @Value("${cloudinary.folder:mediaboard}")
    private String cloudinaryFolder;
    
    // 단일 파일 업로드 (Cloudinary)
    public MediaFileDto uploadFile(Long postId, MultipartFile file) throws IOException {
        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + postId));
        
        // 파일 검증
        if (file.isEmpty()) {
            throw new RuntimeException("빈 파일입니다.");
        }
        
        // 파일 크기 제한 (50MB)
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new RuntimeException("파일 크기가 너무 큽니다. (최대 50MB)");
        }
        
        // 허용된 파일 타입 검증
        String contentType = file.getContentType();
        if (!isAllowedFileType(contentType)) {
            throw new RuntimeException("허용되지 않는 파일 타입입니다: " + contentType);
        }
        
        // Cloudinary에 파일 업로드
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, cloudinaryFolder);
        
        // 업로드 결과에서 정보 추출
        String publicId = (String) uploadResult.get("public_id");
        String fileUrl = (String) uploadResult.get("secure_url");
        String originalFilename = file.getOriginalFilename();
        
        // MediaFile 엔티티 생성 및 저장
        MediaFile mediaFile = new MediaFile(
            post,
            contentType,
            originalFilename,
            publicId, // Cloudinary public_id를 fileName으로 사용
            publicId, // filePath도 public_id로 설정
            file.getSize(),
            fileUrl
        );
        
        MediaFile savedMediaFile = mediaFileRepository.save(mediaFile);
        return new MediaFileDto(savedMediaFile);
    }
    
    // 다중 파일 업로드
    public List<MediaFileDto> uploadFiles(Long postId, MultipartFile[] files) throws IOException {
        List<MediaFileDto> uploadedFiles = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                MediaFileDto uploadedFile = uploadFile(postId, file);
                uploadedFiles.add(uploadedFile);
            }
        }
        
        return uploadedFiles;
    }
    
    // 특정 게시글의 파일 목록 조회
    public List<MediaFileDto> getFilesByPostId(Long postId) {
        List<MediaFile> files = mediaFileRepository.findByPostIdOrderById(postId);
        return files.stream()
                .map(MediaFileDto::new)
                .collect(Collectors.toList());
    }
    
    // 파일 삭제 (Cloudinary)
    public void deleteFile(Long fileId) throws IOException {
        MediaFile mediaFile = mediaFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다. ID: " + fileId));
        
        // Cloudinary에서 파일 삭제
        String publicId = mediaFile.getFileName(); // public_id로 저장됨
        cloudinaryService.deleteFile(publicId);
        
        // DB에서 파일 정보 삭제
        mediaFileRepository.delete(mediaFile);
    }
    
    // 파일 타입 검증
    private boolean isAllowedFileType(String contentType) {
        if (contentType == null) {
            return false;
        }
        
        return contentType.startsWith("image/") || 
               contentType.startsWith("video/") ||
               contentType.equals("application/octet-stream");
    }
    
    // 파일명으로 MediaFile 조회
    public MediaFile getFileByFileName(String fileName) {
        return mediaFileRepository.findByFileName(fileName);
    }
    
    // 파일 바이트 배열 반환 (Cloudinary URL 리다이렉트)
    public String getFileUrl(String fileName) {
        MediaFile mediaFile = mediaFileRepository.findByFileName(fileName);
        if (mediaFile == null) {
            throw new RuntimeException("파일을 찾을 수 없습니다: " + fileName);
        }
        
        return mediaFile.getFileUrl(); // Cloudinary URL 반환
    }
}