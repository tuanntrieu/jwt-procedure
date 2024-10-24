package com.example.demojwt.controller;

import com.example.demojwt.base.VsResponseUtil;
import com.example.demojwt.dto.request.PermissionUpdateRequestDto;
import com.example.demojwt.dto.request.RoleDto;
import com.example.demojwt.service.FunctionService;
import com.example.demojwt.service.PermissionService;
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
    private final FunctionService functionService;
    private final PermissionService permissionService;

    @PostMapping("/create-role")
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        roleService.createRole(roleDto);
        return VsResponseUtil.success("Role created");
    }

    @PostMapping("/delete-role")
    public ResponseEntity<?> deleteRole(@RequestBody RoleDto roleDto) {
        roleService.deleteRole(roleDto);
        return VsResponseUtil.success("Role deleted");
    }

    @PostMapping("/update-permision-in-role")
    public ResponseEntity<?> updateFuncInRole(@RequestBody PermissionUpdateRequestDto request) {
        roleService.updatePermissionInRole(request);
        return VsResponseUtil.success("Permission updated");
    }

}
