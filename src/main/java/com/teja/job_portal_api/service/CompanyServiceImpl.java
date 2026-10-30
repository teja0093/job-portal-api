package com.teja.job_portal_api.service;

import com.teja.job_portal_api.dto.CompanyRequest;
import com.teja.job_portal_api.dto.CompanyResponse;
import com.teja.job_portal_api.entity.Company;
import com.teja.job_portal_api.exception.DuplicateResourceException;
import com.teja.job_portal_api.exception.ResourceNotFoundException;
import com.teja.job_portal_api.repository.CompanyRepository;
import com.teja.job_portal_api.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyResponse create(CompanyRequest request) {
        if (companyRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Company already exists: " + request.name());
        }
        Company company = Company.builder()
                .name(request.name())
                .location(request.location())
                .description(request.description())
                .build();
        return toResponse(companyRepository.save(company));
    }

    @Override
    public List<CompanyResponse> getAll() {
        return companyRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public CompanyResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = findOrThrow(id);
        company.setName(request.name());
        company.setLocation(request.location());
        company.setDescription(request.description());
        return toResponse(companyRepository.save(company));
    }

    @Override
    public void delete(Long id) {
        companyRepository.delete(findOrThrow(id));
    }

    private Company findOrThrow(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    private CompanyResponse toResponse(Company c) {
        return new CompanyResponse(c.getId(), c.getName(), c.getLocation(), c.getDescription());
    }
}