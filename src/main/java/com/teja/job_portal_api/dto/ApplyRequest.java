package com.teja.job_portal_api.dto;

import jakarta.validation.constraints.NotNull;

public record ApplyRequest(
        @NotNull(message = "Candidate id is required")
        Long candidateId) {
}