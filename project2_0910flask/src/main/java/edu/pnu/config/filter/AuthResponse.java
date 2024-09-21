package edu.pnu.config.filter;

import edu.pnu.config.CustomUserDetails;
public class AuthResponse {
    private String token;
    private CustomUserDetails user;
    private String role; // 추가된 필드

    public AuthResponse(String token, CustomUserDetails user) {
        this.token = token;
        this.user = user;
        this.role = user.getRole() != null ? user.getRole().name() : null; // 역할을 문자열로 변환하여 저장
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CustomUserDetails getUser() {
        return user;
    }

    public void setUser(CustomUserDetails user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
