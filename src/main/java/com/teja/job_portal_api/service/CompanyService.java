package com.teja.job_portal_api.service;

import com.teja.job_portal_api.dto.CompanyRequest;
import com.teja.job_portal_api.dto.CompanyResponse;

import java.util.List;

public interface CompanyService {
    CompanyResponse create(CompanyRequest request);
    List<CompanyResponse> getAll();
    CompanyResponse getById(Long id);
    CompanyResponse update(Long id, CompanyRequest request);
    void delete(Long id);
}