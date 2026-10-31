package com.teja.job_portal_api.repository;

import com.teja.job_portal_api.entity.Job;
import com.teja.job_portal_api.entity.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
            SELECT j FROM Job j
            WHERE (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
              AND (:jobType IS NULL OR j.jobType = :jobType)
            """)
    Page<Job> search(@Param("keyword") String keyword,
                     @Param("location") String location,
                     @Param("jobType") JobType jobType,
                     Pageable pageable);
}