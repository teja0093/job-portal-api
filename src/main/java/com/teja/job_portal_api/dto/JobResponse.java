package com.teja.job_portal_api.dto;

import com.teja.job_portal_api.entity.JobType;

import java.time.LocalDate;

public record JobResponse(
        Long id,
        String title,
        String description,
        Double salary,
        String location,
        JobType jobType,
        LocalDate postedDate,
        boolean open,
        Long companyId,
        String companyName) {
}