package com.example.demojwt.controller;

import com.example.demojwt.base.VsResponseUtil;
import com.example.demojwt.dto.request.*;
import com.example.demojwt.security.jwt.JwtTokenProvider;
import com.example.demojwt.service.AuthService;
import com.example.demojwt.service.RoleService;
import com.example.demojwt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/users")
    public ResponseEntity<?> getUser(@RequestParam String username) {
        return VsResponseUtil.success(userService.findByUsername(username));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto) {
        return VsResponseUtil.success(authService.login(requestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        authService.logout(request, response, authentication);
        return VsResponseUtil.success("Logout success");
    }

    @PostMapping("/auth/load-permission-by-role-name")
    public ResponseEntity<?> loadPermissionByRoleName(@RequestBody StudentUpdateRoleDto studentUpdateRoleDto) {
        return VsResponseUtil.success(roleService.loadPermissionsByRole(studentUpdateRoleDto));
    }

    @PostMapping("/auth/load-function-by-role-name")
    public ResponseEntity<?> loadFunctionByRoleName(@RequestBody StudentUpdateRoleDto studentUpdateRoleDto) {
        return VsResponseUtil.success(roleService.loadFunctionsByRole(studentUpdateRoleDto));
    }

    @PostMapping("/auth/find-all-role")
    public ResponseEntity<?> findAllRole() {
        return VsResponseUtil.success(roleService.getALlRole());
    }


    @PostMapping("/auth/load-group")
    public ResponseEntity<?> loadGroup() {
        return VsResponseUtil.success(roleService.loadGroups());
    }

    @PostMapping("/auth/load-permission-by-group")
    public ResponseEntity<?> loadPerByGroup(@RequestBody GroupDto groupDto) {
        return VsResponseUtil.success(roleService.loadPermissionsByGroup(groupDto.getGroup()));
    }

    @PostMapping("/auth/username-exits")
    public ResponseEntity<?> usernameExist(@RequestBody UsernameExistDto dto) {
        return VsResponseUtil.success(userService.existsByUsername(dto.getUsername()));
    }

    @PostMapping("/auth/exist-role")
    public ResponseEntity<?> existRole(@RequestBody RoleDto roleDto) {
        return VsResponseUtil.success(roleService.existsRole(roleDto.getRoleName()));
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshDto dto) {
        return VsResponseUtil.success(jwtTokenProvider.refreshToken(dto.getToken()));
    }


}
