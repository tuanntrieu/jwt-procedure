package com.example.demojwt.service.impl;

import com.example.demojwt.dto.request.LoginRequestDto;
import com.example.demojwt.dto.response.LoginResponseDto;
import com.example.demojwt.enity.Permission;
import com.example.demojwt.enity.TokenInvalid;
import com.example.demojwt.enity.User;
import com.example.demojwt.exception.InvalidException;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.exception.UnauthorizedException;
import com.example.demojwt.repository.PermissionRepository;
import com.example.demojwt.repository.TokenInvalidRepository;
import com.example.demojwt.repository.UserRepository;
import com.example.demojwt.security.CustomUserDetails;
import com.example.demojwt.security.jwt.JwtTokenProvider;
import com.example.demojwt.service.AuthService;
import com.example.demojwt.service.FunctionService;
import com.example.demojwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenInvalidRepository tokenInvalidRepository;
    private final UserService userService;
    private final FunctionService functionService;


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateToken(userDetails, Boolean.FALSE);
            String refreshToken = jwtTokenProvider.generateToken(userDetails, Boolean.TRUE);
            User user = userService.findByUsername(loginRequestDto.getUsername());
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            userService.save(user);
            return LoginResponseDto.builder()
                    .username(user.getUsername())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .role(user.getRole().getRoleName())
                    .functions(functionService.loadFunctionResponseByRole(user.getRole().getRoleName()))
                    .build();
        } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
            throw new InvalidException("Incorrect username or password");
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextLogoutHandler logout = new SecurityContextLogoutHandler();
        String token;
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
            String username = jwtTokenProvider.extractSubjectFromJwt(token);
            String refreshToken = userService.findByUsername(username).getRefreshToken();

            tokenInvalidRepository.save(TokenInvalid.builder()
                    .token(token)
                    .tokenType("access_token")
                    .expiresTime(jwtTokenProvider.extractExpiresTimeFromJwt(token))
                    .build());
            tokenInvalidRepository.save(TokenInvalid.builder()
                    .token(refreshToken)
                    .tokenType("refresh_token")
                    .expiresTime(jwtTokenProvider.extractExpiresTimeFromJwt(refreshToken))
                    .build());
        }
        logout.logout(request, response, authentication);
    }

    @Override
    public Boolean usernameExists(String username) {
        return userService.existsByUsername(username);
    }


}
