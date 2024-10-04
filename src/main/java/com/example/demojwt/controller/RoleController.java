package com.example.demojwt.controller;

import com.example.demojwt.base.VsResponseUtil;
import com.example.demojwt.dto.request.PermisionUpdateRequest;
import com.example.demojwt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/roles/update-permission")
    public ResponseEntity<?> updatePermission(@RequestBody PermisionUpdateRequest permisionUpdateRequest) {
        roleService.updatePermission(permisionUpdateRequest);
        return VsResponseUtil.success("Permission updated");
    }
}
