package com.example.demojwt.service.impl;

import com.example.demojwt.repository.PermissionRepository;
import com.example.demojwt.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

}
