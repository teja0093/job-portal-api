package com.teja.job_portal_api.controller;

import com.teja.job_portal_api.dto.ApplicationResponse;
import com.teja.job_portal_api.dto.StatusUpdateRequest;
import com.teja.job_portal_api.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService applicationService;

    @PostMapping("/api/jobs/{jobId}/apply")
    public ResponseEntity<ApplicationResponse> apply(@PathVariable Long jobId,
                                                     Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.apply(jobId, authentication.getName()));
    }

    @GetMapping("/api/applications/candidate/{candidateId}")
    public List<ApplicationResponse> byCandidate(@PathVariable Long candidateId) {
        return applicationService.getByCandidate(candidateId);
    }

    @GetMapping("/api/jobs/{jobId}/applications")
    public List<ApplicationResponse> byJob(@PathVariable Long jobId) {
        return applicationService.getByJob(jobId);
    }

    @PatchMapping("/api/applications/{id}/status")
    public ApplicationResponse updateStatus(@PathVariable Long id,
                                            @Valid @RequestBody StatusUpdateRequest request) {
        return applicationService.updateStatus(id, request.status());
    }
    @GetMapping("/api/applications/my")
    public List<ApplicationResponse> myApplications(Authentication authentication) {
        return applicationService.getByCandidateEmail(authentication.getName());
    }
}