package com.example.mgdSpringReact.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

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
        private List<String> organizations;

        public AuthResponse(String token, String username, String roles, List<String> organizations) {
            this.token = token;
            this.username = username;
            this.roles = roles;
            this.organizations = organizations;
        }
    }
}
