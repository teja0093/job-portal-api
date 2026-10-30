package com.teja.job_portal_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        String name,
        String location,
        @Size(max = 1000, message = "Description must be under 1000 characters")
        String description) {
}