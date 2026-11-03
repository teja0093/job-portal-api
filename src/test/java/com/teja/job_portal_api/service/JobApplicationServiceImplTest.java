package com.teja.job_portal_api.service;

import com.teja.job_portal_api.dto.ApplicationResponse;
import com.teja.job_portal_api.entity.*;
import com.teja.job_portal_api.exception.BusinessRuleException;
import com.teja.job_portal_api.exception.DuplicateResourceException;
import com.teja.job_portal_api.exception.ResourceNotFoundException;
import com.teja.job_portal_api.repository.JobApplicationRepository;
import com.teja.job_portal_api.repository.JobRepository;
import com.teja.job_portal_api.repository.UserRepository;
import com.teja.job_portal_api.service.impl.JobApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository applicationRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobApplicationServiceImpl service;

    private Job openJob;
    private User candidate;

    @BeforeEach
    void setUp() {
        Company company = Company.builder().id(1L).name("Infosys").build();
        openJob = Job.builder().id(1L).title("Java Developer").open(true).company(company).build();
        candidate = User.builder().id(10L).name("Josh").email("josh@gmail.com")
                .role(Role.CANDIDATE).build();
    }

    @Test
    void apply_success() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(openJob));
        when(userRepository.findByEmail("josh@gmail.com")).thenReturn(Optional.of(candidate));
        when(applicationRepository.existsByJobIdAndCandidateId(1L, 10L)).thenReturn(false);
        when(applicationRepository.save(any(JobApplication.class))).thenAnswer(inv -> {
            JobApplication a = inv.getArgument(0);
            a.setId(100L);
            return a;
        });

        ApplicationResponse response = service.apply(1L, "josh@gmail.com");

        assertEquals(100L, response.id());
        assertEquals(ApplicationStatus.APPLIED, response.status());
        assertEquals("Josh", response.candidateName());
        verify(applicationRepository).save(any(JobApplication.class));
    }

    @Test
    void apply_jobNotFound_throws404() {
        when(jobRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.apply(99L, "josh@gmail.com"));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void apply_closedJob_throwsBusinessRule() {
        openJob.setOpen(false);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(openJob));
        when(userRepository.findByEmail("josh@gmail.com")).thenReturn(Optional.of(candidate));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.apply(1L, "josh@gmail.com"));
        assertEquals("This job is closed", ex.getMessage());
    }

    @Test
    void apply_alreadyApplied_throwsDuplicate() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(openJob));
        when(userRepository.findByEmail("josh@gmail.com")).thenReturn(Optional.of(candidate));
        when(applicationRepository.existsByJobIdAndCandidateId(1L, 10L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.apply(1L, "josh@gmail.com"));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void apply_employer_throwsBusinessRule() {
        User employer = User.builder().id(20L).name("HR").email("hr@example.com")
                .role(Role.EMPLOYER).build();
        when(jobRepository.findById(1L)).thenReturn(Optional.of(openJob));
        when(userRepository.findByEmail("hr@example.com")).thenReturn(Optional.of(employer));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.apply(1L, "hr@example.com"));
        assertEquals("Only candidates can apply for jobs", ex.getMessage());
    }
}