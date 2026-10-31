package com.teja.job_portal_api.dto;

import com.teja.job_portal_api.entity.ApplicationStatus;

import java.time.LocalDate;

public record ApplicationResponse(
        Long id,
        ApplicationStatus status,
        LocalDate appliedDate,
        Long jobId,
        String jobTitle,
        Long candidateId,
        String candidateName) {
}