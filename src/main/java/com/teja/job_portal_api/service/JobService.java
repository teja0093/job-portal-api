package com.teja.job_portal_api.service;

import com.teja.job_portal_api.dto.JobRequest;
import com.teja.job_portal_api.dto.JobResponse;
import com.teja.job_portal_api.entity.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {
    JobResponse create(JobRequest request);
    Page<JobResponse> search(String keyword, String location, JobType jobType, Pageable pageable);
    JobResponse getById(Long id);
    JobResponse update(Long id, JobRequest request);
    void delete(Long id);
}