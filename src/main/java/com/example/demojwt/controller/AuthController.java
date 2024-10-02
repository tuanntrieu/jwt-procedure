package com.example.demojwt.controller;

import com.example.demojwt.base.VsResponseUtil;
import com.example.demojwt.dto.request.LoginRequestDto;
import com.example.demojwt.enity.User;
import com.example.demojwt.enums.Role;
import com.example.demojwt.repository.UserRepository;
import com.example.demojwt.service.AuthService;
import com.example.demojwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    public  final UserService userService ;
    private final AuthService authService ;


    @PostMapping("/users")
    public ResponseEntity<?> getUser(@RequestParam String username){
        return VsResponseUtil.success(userService.findByUsername(username));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto){
        
        return VsResponseUtil.success(authService.login(requestDto));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        authService.logout(request, response, authentication);
        return VsResponseUtil.success("Logout success");
    }

}
