package com.example.demojwt.service.impl;

import com.example.demojwt.dto.request.GroupDto;
import com.example.demojwt.dto.request.PermissionUpdateRequest;
import com.example.demojwt.dto.request.RoleDto;
import com.example.demojwt.dto.request.StudentUpdateRoleDto;
import com.example.demojwt.enity.*;
import com.example.demojwt.exception.DataIntegrityViolationException;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.repository.FunctionRepository;
import com.example.demojwt.repository.PermissionRepository;
import com.example.demojwt.repository.RoleRepository;
import com.example.demojwt.repository.UserRepository;
import com.example.demojwt.service.RoleService;
import com.example.demojwt.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final StudentService studentService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FunctionRepository functionRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void updatePermissionInRole(PermissionUpdateRequest request) {
        Set<Permission> newPermissions = new HashSet<>();
        Set<Function> newFunctions = new HashSet<>();
        Role role = findRoleByRoleName(request.getRole());
        role.getPermissions().forEach(permission -> {
            permissionRepository.deleteByRole(role.getId(), permission.getId());
        });
        role.getFunctions().forEach(function -> {
            functionRepository.deleteByRole(role.getId(), function.getId());
        });
        request.getPermissions().forEach(permission -> {
            Permission permissionTmp = permissionRepository.findByNamePermission(permission);
            permissionTmp.getRoles().add(role);
            permissionRepository.save(permissionTmp);
            newPermissions.add(permissionRepository.findByNamePermission(permission));
            Function newFunc = permissionTmp.getFunction();
            if (!newFunctions.contains(newFunc)) {
                newFunctions.add(newFunc);
                newFunc.getRoles().add(role);
                permissionRepository.save(permissionTmp);
            }
        });
        role.setFunctions(newFunctions);
        role.setPermissions(newPermissions);
        roleRepository.save(role);
    }

    @Override
    public List<String> getALlRole() {
        return roleRepository.findAllRoleName();
    }

    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new NotFoundException("Role not found")
        );
    }

    @Override
    public void createRole(RoleDto role) {
        if (roleRepository.existsByRoleName(role.getRoleName())) {
            throw new DataIntegrityViolationException("Role already exists");
        }
        Role newRole = new Role();
        newRole.setRoleName("ROLE_" + role.getRoleName().toUpperCase());
        roleRepository.save(newRole);
    }

    @Override
    public void deleteRole(RoleDto role) {
        if (!roleRepository.existsByRoleName(role.getRoleName())) {
            throw new NotFoundException("Role not found");
        }
        Role roleTmp = findRoleByRoleName(role.getRoleName());
        roleTmp.getUsers().forEach(user -> {
            user.setRole(null);
            userRepository.save(user);
        });
        roleRepository.deleteRoleInFunction(roleTmp.getId());
        roleRepository.deleteRoleInPer(roleTmp.getId());

        roleRepository.deleteByRoleName(role.getRoleName());
    }

    @Override
    public List<GroupDto> loadGroups() {
        return permissionRepository.loadGroupPermission().stream()
                .map(group -> new GroupDto(group))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> loadPermissionsByGroup(String group) {
        return permissionRepository.findPermissionByGr(group);
    }


    @Override
    public List<String> loadPermissionsByRole(StudentUpdateRoleDto dto) {
        return permissionRepository.loadPermissionsByRole(dto.getRole());
    }

    @Override
    public List<String> loadFunctionsByRole(StudentUpdateRoleDto dto) {
        return functionRepository.loadFunctionUrlByRoleName(dto.getRole());
    }

    @Override
    public boolean existsRole(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

    @Override
    public void updateRole(StudentUpdateRoleDto studentUpdateRoleDto) {
        Student student = studentService.findById(studentUpdateRoleDto.getStudentId());
        User user = student.getUser();
        user.setRole(findRoleByRoleName(studentUpdateRoleDto.getRole()));
        userRepository.save(user);
    }
}
