package com.teja.job_portal_api.service.impl;

import com.teja.job_portal_api.dto.ApplicationResponse;
import com.teja.job_portal_api.entity.*;
import com.teja.job_portal_api.exception.BusinessRuleException;
import com.teja.job_portal_api.exception.DuplicateResourceException;
import com.teja.job_portal_api.exception.ResourceNotFoundException;
import com.teja.job_portal_api.repository.JobApplicationRepository;
import com.teja.job_portal_api.repository.JobRepository;
import com.teja.job_portal_api.repository.UserRepository;
import com.teja.job_portal_api.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public ApplicationResponse apply(Long jobId, String candidateEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + candidateEmail));

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new BusinessRuleException("Only candidates can apply for jobs");
        }
        if (!job.isOpen()) {
            throw new BusinessRuleException("This job is closed");
        }
        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidate.getId())) {
            throw new DuplicateResourceException("You have already applied for this job");
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .candidate(candidate)
                .status(ApplicationStatus.APPLIED)
                .appliedDate(LocalDate.now())
                .build();
        return toResponse(applicationRepository.save(application));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getByCandidate(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getByCandidateEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        return applicationRepository.findByCandidateId(user.getId()).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId).stream().map(this::toResponse).toList();
    }

    @Override
    public ApplicationResponse updateStatus(Long applicationId, ApplicationStatus status) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        application.setStatus(status);
        return toResponse(applicationRepository.save(application));
    }

    private ApplicationResponse toResponse(JobApplication a) {
        return new ApplicationResponse(a.getId(), a.getStatus(), a.getAppliedDate(),
                a.getJob().getId(), a.getJob().getTitle(),
                a.getCandidate().getId(), a.getCandidate().getName());
    }
}