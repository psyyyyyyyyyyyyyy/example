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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileUploadService {
    
    @Autowired
    private MediaFileRepository mediaFileRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    // 파일 저장 경로 (application.properties에서 설정 가능)
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    // 서버 URL
    @Value("${server.url:http://localhost:8080}")
    private String serverUrl;
    
    // 단일 파일 업로드
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
        
        // 업로드 디렉토리 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // 고유한 파일명 생성
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // 파일 저장
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // 파일 URL 생성
        String fileUrl = serverUrl + "/api/files/" + uniqueFilename;
        
        // MediaFile 엔티티 생성 및 저장
        MediaFile mediaFile = new MediaFile(
            post,
            contentType,
            originalFilename,
            uniqueFilename,
            filePath.toString(),
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
    
    // 파일 삭제
    public void deleteFile(Long fileId) throws IOException {
        MediaFile mediaFile = mediaFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다. ID: " + fileId));
        
        // 실제 파일 삭제
        Path filePath = Paths.get(mediaFile.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        
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
               contentType.equals("application/octet-stream"); // 일부 브라우저에서 video 파일을 이렇게 보내기도 함
    }
    
    // 파일 확장자 추출
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
    
    // 파일명으로 MediaFile 조회
    public MediaFile getFileByFileName(String fileName) {
        return mediaFileRepository.findByFileName(fileName);
    }
    
    // 파일 바이트 배열 반환 (파일 다운로드용)
    public byte[] getFileBytes(String fileName) throws IOException {
        MediaFile mediaFile = mediaFileRepository.findByFileName(fileName);
        if (mediaFile == null) {
            throw new RuntimeException("파일을 찾을 수 없습니다: " + fileName);
        }
        
        Path filePath = Paths.get(mediaFile.getFilePath());
        if (!Files.exists(filePath)) {
            throw new RuntimeException("파일이 존재하지 않습니다: " + fileName);
        }
        
        return Files.readAllBytes(filePath);
    }
}
