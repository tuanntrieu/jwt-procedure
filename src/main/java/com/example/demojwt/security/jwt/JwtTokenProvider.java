package com.example.demojwt.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demojwt.enity.User;
import com.example.demojwt.exception.InvalidException;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.repository.UserRepository;
import com.example.demojwt.security.CustomUserDetails;
import com.example.demojwt.service.TokenService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String CLAIM_TYPE = "type";
    private static final String USERNAME_KEY = "username";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_ID = "id_token";
    private final UserRepository userRepository;
    private final TokenService tokenService;
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration_time}")
    private Integer expirationTimeAccessToken;
    @Value("${jwt.refresh.expiration_time}")
    private Integer expirationTimeRefreshToken;

    public String generateToken(CustomUserDetails userDetails, Boolean isRefreshToken) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_TYPE, "access");
        claims.put(USERNAME_KEY, userDetails.getUsername());
        claims.put(AUTHORITIES_KEY, authorities);

        if (isRefreshToken) {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (expirationTimeRefreshToken * 60 * 1000L)))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (expirationTimeAccessToken * 60 * 1000L)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String extractSubjectFromJwt(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }
    public Date extractExpiresTimeFromJwt(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }

    public String refreshToken(String refreshToken) {
        if (!validateToken(refreshToken) || tokenService.exists(refreshToken)) {
            throw new InvalidException("Invalid Refresh Token");
        }
        String username = extractSubjectFromJwt(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new InvalidException("Invalid Refresh Token");
        }
        CustomUserDetails userPrincipal = CustomUserDetails.create(user);

        String newAccessToken = generateToken(userPrincipal, Boolean.FALSE);

        String newRefreshToken = generateToken(userPrincipal, Boolean.TRUE);


        user.setRefreshToken(newRefreshToken);
        user.setAccessToken(newAccessToken);
        userRepository.save(user);

        return newAccessToken;
    }

    public boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
}
