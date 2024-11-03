package com.example.demojwt.service;

import com.example.demojwt.enity.User;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


public interface UserService {
    User findByUsername(String username);
    User findById(Long id);

    boolean existsByUsername(String username);

    User save(User user);
}
