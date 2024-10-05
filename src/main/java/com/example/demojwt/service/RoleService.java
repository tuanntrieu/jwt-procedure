package com.example.demojwt.service;

import com.example.demojwt.dto.request.PermisionUpdateRequest;
import com.example.demojwt.dto.request.StudentUpdateRoleDto;
import com.example.demojwt.enity.Permission;
import com.example.demojwt.enity.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface RoleService {
    void updatePermission(PermisionUpdateRequest permisionUpdateRequest);

    List<String> getALlRole();

    void updateRole(StudentUpdateRoleDto studentUpdateRoleDto);

    Role findRoleByRoleName(String roleName);
}
