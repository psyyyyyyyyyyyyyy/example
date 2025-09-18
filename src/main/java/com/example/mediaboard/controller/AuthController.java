package com.example.mediaboard.controller;

import com.example.mediaboard.dto.LoginRequest;
import com.example.mediaboard.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AuthController {

    // 하드코딩된 계정 정보
    private static final String HARDCODED_USERNAME = "admin";
    private static final String HARDCODED_PASSWORD = "password123";

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            // 입력값 검증
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(LoginResponse.failure("사용자명을 입력해주세요."));
            }

            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(LoginResponse.failure("비밀번호를 입력해주세요."));
            }

            // 하드코딩된 계정과 비교
            if (HARDCODED_USERNAME.equals(username.trim()) && 
                HARDCODED_PASSWORD.equals(password)) {
                
                // 간단한 토큰 생성 (실제로는 JWT 등을 사용해야 함)
                String token = "token_" + UUID.randomUUID().toString().substring(0, 8);
                
                return ResponseEntity.ok(LoginResponse.success(username, token));
            } else {
                return ResponseEntity.badRequest()
                    .body(LoginResponse.failure("사용자명 또는 비밀번호가 올바르지 않습니다."));
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(LoginResponse.failure("서버 오류가 발생했습니다."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout() {
        // 간단한 로그아웃 응답
        return ResponseEntity.ok(new LoginResponse(true, "로그아웃 되었습니다.", null, null));
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> checkAuth(@RequestHeader(value = "Authorization", required = false) String token) {
        // 간단한 토큰 검증 (실제로는 더 복잡한 검증 필요)
        if (token != null && token.startsWith("Bearer token_")) {
            return ResponseEntity.ok(new LoginResponse(true, "인증됨", "admin", token.substring(7)));
        } else {
            return ResponseEntity.badRequest()
                .body(LoginResponse.failure("인증되지 않음"));
        }
    }
}
