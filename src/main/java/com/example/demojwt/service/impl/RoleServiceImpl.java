package com.example.demojwt.service.impl;

import com.example.demojwt.dto.request.PermisionUpdateRequest;
import com.example.demojwt.enity.Permission;
import com.example.demojwt.enity.Role;
import com.example.demojwt.repository.PermissionRepository;
import com.example.demojwt.repository.RoleRepository;
import com.example.demojwt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    @Override
    public void updatePermission(PermisionUpdateRequest permisionUpdateRequest) {
        Set<Permission> newPermission = new HashSet<>();

        Role role=roleRepository.findByRoleName(permisionUpdateRequest.getRole());

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
}
