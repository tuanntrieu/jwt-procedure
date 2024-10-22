package com.example.demojwt.service;


import com.example.demojwt.dto.request.LoginRequestDto;
import com.example.demojwt.dto.response.LoginResponseDto;
import com.example.demojwt.enity.Permission;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
    Boolean usernameExists(String username);


}
