package com.teja.job_portal_api.dto;

import com.teja.job_portal_api.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull(message = "Status is required")
        ApplicationStatus status) {
}