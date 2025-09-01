package com.example.mgdSpringReact.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDtos {

    @Data
    public static class RegisterRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String username;
        private String roles;

        public AuthResponse(String token, String username, String roles) {
            this.token = token;
            this.username = username;
            this.roles = roles;
        }
    }
}
