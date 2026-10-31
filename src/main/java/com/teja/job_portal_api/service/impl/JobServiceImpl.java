package com.teja.job_portal_api.service.impl;

import com.teja.job_portal_api.dto.JobRequest;
import com.teja.job_portal_api.dto.JobResponse;
import com.teja.job_portal_api.entity.Company;
import com.teja.job_portal_api.entity.Job;
import com.teja.job_portal_api.entity.JobType;
import com.teja.job_portal_api.exception.ResourceNotFoundException;
import com.teja.job_portal_api.repository.CompanyRepository;
import com.teja.job_portal_api.repository.JobRepository;
import com.teja.job_portal_api.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    @Override
    public JobResponse create(JobRequest request) {
        Company company = findCompany(request.companyId());
        Job job = Job.builder()
                .title(request.title())
                .description(request.description())
                .salary(request.salary())
                .location(request.location())
                .jobType(request.jobType())
                .postedDate(LocalDate.now())
                .company(company)
                .build();
        return toResponse(jobRepository.save(job));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobResponse> search(String keyword, String location, JobType jobType, Pageable pageable) {
        return jobRepository.search(blankToNull(keyword), blankToNull(location), jobType, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getById(Long id) {
        return toResponse(findJob(id));
    }

    @Override
    public JobResponse update(Long id, JobRequest request) {
        Job job = findJob(id);
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setSalary(request.salary());
        job.setLocation(request.location());
        job.setJobType(request.jobType());
        job.setCompany(findCompany(request.companyId()));
        return toResponse(jobRepository.save(job));
    }

    @Override
    public void delete(Long id) {
        jobRepository.delete(findJob(id));
    }

    private Job findJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }

    private Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private JobResponse toResponse(Job j) {
        return new JobResponse(j.getId(), j.getTitle(), j.getDescription(), j.getSalary(),
                j.getLocation(), j.getJobType(), j.getPostedDate(), j.isOpen(),
                j.getCompany().getId(), j.getCompany().getName());
    }
}