package com.teja.job_portal_api.service;

import com.teja.job_portal_api.dto.RegisterRequest;
import com.teja.job_portal_api.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse register(RegisterRequest request);
    UserResponse getById(Long id);
    List<UserResponse> getAll();
}