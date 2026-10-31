package com.teja.job_portal_api.controller;

import com.teja.job_portal_api.dto.JobRequest;
import com.teja.job_portal_api.dto.JobResponse;
import com.teja.job_portal_api.entity.JobType;
import com.teja.job_portal_api.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> create(@Valid @RequestBody JobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.create(request));
    }

    @GetMapping
    public Page<JobResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @PageableDefault(size = 10, sort = "postedDate") Pageable pageable) {
        return jobService.search(keyword, location, jobType, pageable);
    }

    @GetMapping("/{id}")
    public JobResponse getById(@PathVariable Long id) {
        return jobService.getById(id);
    }

    @PutMapping("/{id}")
    public JobResponse update(@PathVariable Long id, @Valid @RequestBody JobRequest request) {
        return jobService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}























