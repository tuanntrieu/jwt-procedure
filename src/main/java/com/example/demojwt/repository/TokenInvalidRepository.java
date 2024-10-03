package com.example.demojwt.repository;

import com.example.demojwt.enity.TokenInvalid;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TokenInvalidRepository  extends JpaRepository <TokenInvalid, Long> {
    boolean existsByToken(String token);

    @Transactional
    @Modifying
    int deleteByExpiresTimeBefore(Date expiresTime);
}
