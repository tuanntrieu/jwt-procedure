package com.example.demojwt.service;

import com.example.demojwt.dto.request.GroupDto;
import com.example.demojwt.dto.request.PermissionUpdateRequest;
import com.example.demojwt.dto.request.RoleDto;
import com.example.demojwt.dto.request.StudentUpdateRoleDto;
import com.example.demojwt.enity.Function;
import com.example.demojwt.enity.Permission;
import com.example.demojwt.enity.Role;

import java.util.List;

public interface RoleService {
    void updatePermissionInRole(PermissionUpdateRequest request);

    List<String> getALlRole();

    void updateRole(StudentUpdateRoleDto studentUpdateRoleDto);

    Role findRoleByRoleName(String roleName);

    void createRole(RoleDto role);

    void deleteRole(RoleDto role);

    List<GroupDto> loadGroups();

    List<String> loadPermissionsByGroup(String group);

    List<String> loadPermissionsByRole(StudentUpdateRoleDto dto );
    List<String> loadFunctionsByRole(StudentUpdateRoleDto dto );
    boolean existsRole(String roleName);
}
