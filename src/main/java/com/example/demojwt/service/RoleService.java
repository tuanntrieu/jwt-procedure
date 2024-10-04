package com.example.demojwt.service;

import com.example.demojwt.dto.request.PermisionUpdateRequest;
import com.example.demojwt.enity.Permission;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface RoleService {
    void updatePermission(PermisionUpdateRequest permisionUpdateRequest);
}
