package com.example.demojwt.repository;

import com.example.demojwt.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT u FROM User u WHERE u.username=?1")
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}