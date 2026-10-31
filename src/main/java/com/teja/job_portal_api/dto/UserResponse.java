package com.teja.job_portal_api.dto;

import com.teja.job_portal_api.entity.Role;

public record UserResponse(Long id, String name, String email, Role role) {
}