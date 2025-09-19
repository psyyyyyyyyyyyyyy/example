package com.example.mediaboard.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    /**
     * 파일을 Cloudinary에 업로드
     */
    public Map<String, Object> uploadFile(MultipartFile file, String folder) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "auto", // 이미지, 비디오 자동 감지
                "use_filename", true,
                "unique_filename", true
        ));
    }

    /**
     * 파일을 Cloudinary에서 삭제
     */
    public Map<String, Object> deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    /**
     * 이미지 URL 생성 (변환 옵션 포함)
     */
    public String generateImageUrl(String publicId, int width, int height) {
        return cloudinary.url()
                .transformation(new Transformation().width(width).height(height).crop("fill").quality("auto"))
                .generate(publicId);
    }

    /**
     * 원본 파일 URL 생성
     */
    public String generateUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
}
