package com.example.demojwt.repository;

import com.example.demojwt.enity.Permission;
import com.example.demojwt.enity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);

    @Query("SELECT r.roleName FROM Role r WHERE r.id<>1")
    List<String> findAllRoleName();

    @Query("SELECT r.permissions FROM Role r WHERE r.roleName=?1")
    List<Permission> findPermissionByRoleName(String roleName);

}
