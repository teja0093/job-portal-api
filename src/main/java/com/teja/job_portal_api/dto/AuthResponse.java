package com.teja.job_portal_api.dto;

public record AuthResponse(String token, String email, String role) {
}