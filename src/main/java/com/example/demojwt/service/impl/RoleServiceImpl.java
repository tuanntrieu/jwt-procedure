package com.example.demojwt.service.impl;

import com.example.demojwt.dto.request.PermisionUpdateRequest;
import com.example.demojwt.dto.request.StudentUpdateRoleDto;
import com.example.demojwt.enity.Permission;
import com.example.demojwt.enity.Role;
import com.example.demojwt.enity.Student;
import com.example.demojwt.enity.User;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.repository.PermissionRepository;
import com.example.demojwt.repository.RoleRepository;
import com.example.demojwt.repository.StudentRepository;
import com.example.demojwt.repository.UserRepository;
import com.example.demojwt.service.RoleService;
import com.example.demojwt.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final StudentService studentService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void updatePermission(PermisionUpdateRequest permisionUpdateRequest) {
        Set<Permission> newPermission = new HashSet<>();

        Role role=findRoleByRoleName(permisionUpdateRequest.getRole());

        role.getPermissions().forEach(permission -> {
            permissionRepository.deleteByRoleName(role.getId(), permission.getId());
        });

        permisionUpdateRequest.getPermissions().forEach(permission -> {
            Permission permissionTmp=permissionRepository.findByNamePermission(permission);
            permissionTmp.getRoles().add(role);
            permissionRepository.save(permissionTmp);
            newPermission.add(permissionRepository.findByNamePermission(permission));
        });
        role.setPermissions(newPermission);
        roleRepository.save(role);
    }

    @Override
    public List<String> getALlRole() {
        return roleRepository.findAllRoleName();
    }

    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(
                ()->new NotFoundException("Role not found")
        );
    }

    @Override
    public void updateRole(StudentUpdateRoleDto studentUpdateRoleDto){
        Student student= studentService.findById(studentUpdateRoleDto.getStudentId());
        User user=student.getUser();
        user.setRole(findRoleByRoleName(studentUpdateRoleDto.getRole()));
        userRepository.save(user);
    }
}
