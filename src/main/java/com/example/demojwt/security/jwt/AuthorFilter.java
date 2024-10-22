package com.example.demojwt.security.jwt;

import com.example.demojwt.base.RestData;
import com.example.demojwt.repository.PermissionRepository;
import com.example.demojwt.repository.UserRepository;
import com.example.demojwt.security.CustomUserDetails;
import com.example.demojwt.service.CustomUserDetailsService;
import com.example.demojwt.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PermissionRepository permissionRepository;
    private static final List<String> PUBLIC_END_POINT = List.of(
            "http://localhost:8080/api/v1/auth"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestUrl = request.getRequestURL().toString();
        if (isEndPointAllowed(requestUrl.trim(), PUBLIC_END_POINT)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt) && !tokenService.exists(jwt)) {
                String username = jwtTokenProvider.extractSubjectFromJwt(jwt);
                CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                String role = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst().orElse("ROLE_STUDENT");

                List<String> allowedEndPoints = permissionRepository.allowedEndPoints(role);
                allowedEndPoints.add("http://localhost:8080/api/v1/logout");
                if (!isEndPointAllowed(requestUrl.trim(), allowedEndPoints)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(RestData.error(HttpStatus.FORBIDDEN.value(), "Access Denied")));
                    return;
                }

            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isEndPointAllowed(String requestUrl, List<String> allowedEndPoints) {
        requestUrl = requestUrl.trim();
        for (String endPoint : allowedEndPoints) {
            endPoint = endPoint.trim();
            if (requestUrl.equalsIgnoreCase(endPoint) || requestUrl.startsWith(endPoint)) {
                return true;
            }
        }
        return false;
    }
}
