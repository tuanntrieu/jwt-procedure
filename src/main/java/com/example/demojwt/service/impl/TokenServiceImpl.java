package com.example.demojwt.service.impl;

import com.example.demojwt.repository.TokenInvalidRepository;
import com.example.demojwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenInvalidRepository tokenInvalidRepository;
    @Override
    public boolean exists(String token) {
        return tokenInvalidRepository.existsByToken(token);
    }
}
