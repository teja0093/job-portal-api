package com.teja.job_portal_api.repository;

import com.teja.job_portal_api.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    List<JobApplication> findByCandidateId(Long candidateId);
    List<JobApplication> findByJobId(Long jobId);
}