package com.example.mediaboard.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private String username;
    private String token; // 간단한 토큰 (실제로는 JWT 등을 사용)

    // 기본 생성자
    public LoginResponse() {}

    // 생성자
    public LoginResponse(boolean success, String message, String username, String token) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.token = token;
    }

    // 성공 응답을 위한 정적 메서드
    public static LoginResponse success(String username, String token) {
        return new LoginResponse(true, "로그인 성공", username, token);
    }

    // 실패 응답을 위한 정적 메서드
    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message, null, null);
    }

    // Getter와 Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
