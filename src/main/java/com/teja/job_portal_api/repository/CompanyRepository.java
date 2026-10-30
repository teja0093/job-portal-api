package com.teja.job_portal_api.repository;

import com.teja.job_portal_api.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String name);
}