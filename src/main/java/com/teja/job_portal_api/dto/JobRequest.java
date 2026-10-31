package com.teja.job_portal_api.dto;

import com.teja.job_portal_api.entity.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record JobRequest(
        @NotBlank(message = "Title is required")
        String title,
        @Size(max = 2000, message = "Description must be under 2000 characters")
        String description,
        @Positive(message = "Salary must be positive")
        Double salary,
        String location,
        @NotNull(message = "Job type is required")
        JobType jobType,
        @NotNull(message = "Company id is required")
        Long companyId) {
}