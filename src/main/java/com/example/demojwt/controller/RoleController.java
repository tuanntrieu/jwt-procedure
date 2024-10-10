package com.example.demojwt.controller;

import com.example.demojwt.base.VsResponseUtil;
import com.example.demojwt.dto.request.PermisionUpdateRequest;
import com.example.demojwt.dto.request.StudentUpdateRoleDto;
import com.example.demojwt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/update-permission")
    public ResponseEntity<?> updatePermission(@RequestBody PermisionUpdateRequest permisionUpdateRequest) {
        roleService.updatePermission(permisionUpdateRequest);
        return VsResponseUtil.success("Permission updated");
    }

    @PostMapping("/find-all-role")
    public ResponseEntity<?> findAllRole() {
        return VsResponseUtil.success(roleService.getALlRole());
    }
    @PostMapping("/update-role")
    public ResponseEntity<?> updateRole(@RequestBody StudentUpdateRoleDto studentUpdateRoleDto) {
        roleService.updateRole(studentUpdateRoleDto);
        return VsResponseUtil.success("Role updated");
    }


    @PostMapping("/load-permissions")
    public ResponseEntity<?> loadAllPermission() {
        return VsResponseUtil.success( roleService.getAllPermission());
    }
}
