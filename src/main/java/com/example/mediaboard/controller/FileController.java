package com.example.mediaboard.controller;

import com.example.mediaboard.dto.MediaFileDto;
import com.example.mediaboard.entity.MediaFile;
import com.example.mediaboard.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "파일 API", description = "파일 업로드, 다운로드 관련 API")
public class FileController {
    
    @Autowired
    private FileUploadService fileUploadService;
    
    // 단일 파일 업로드
    @Operation(summary = "단일 파일 업로드", description = "특정 게시글에 이미지 또는 동영상 파일을 업로드합니다. (최대 50MB)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "파일 업로드 성공", 
                    content = @Content(schema = @Schema(implementation = MediaFileDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 파일 또는 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/posts/{postId}/files")
    public ResponseEntity<MediaFileDto> uploadFile(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "업로드할 파일", required = true) @RequestParam("file") MultipartFile file) {
        try {
            MediaFileDto uploadedFile = fileUploadService.uploadFile(postId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedFile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 다중 파일 업로드
    @PostMapping("/posts/{postId}/files/multiple")
    public ResponseEntity<List<MediaFileDto>> uploadFiles(
            @PathVariable Long postId,
            @RequestParam("files") MultipartFile[] files) {
        try {
            List<MediaFileDto> uploadedFiles = fileUploadService.uploadFiles(postId, files);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedFiles);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 특정 게시글의 파일 목록 조회
    @GetMapping("/posts/{postId}/files")
    public ResponseEntity<List<MediaFileDto>> getFilesByPostId(@PathVariable Long postId) {
        try {
            List<MediaFileDto> files = fileUploadService.getFilesByPostId(postId);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 파일 다운로드/조회
    @Operation(summary = "파일 조회/다운로드", description = "업로드된 파일을 조회하거나 다운로드합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파일 조회 성공"),
        @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/files/{fileName}")
    public ResponseEntity<Void> getFile(
            @Parameter(description = "파일명", required = true) @PathVariable String fileName) {
        try {
            String fileUrl = fileUploadService.getFileUrl(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, fileUrl);
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 파일 삭제
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        try {
            fileUploadService.deleteFile(fileId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
