package com.teja.job_portal_api.service;

import com.teja.job_portal_api.dto.ApplicationResponse;
import com.teja.job_portal_api.entity.ApplicationStatus;

import java.util.List;

public interface JobApplicationService {
    ApplicationResponse apply(Long jobId, String candidateEmail);
    List<ApplicationResponse> getByCandidate(Long candidateId);
    List<ApplicationResponse> getByJob(Long jobId);
    ApplicationResponse updateStatus(Long applicationId, ApplicationStatus status);
}