package com.example.demojwt.repository;

import com.example.demojwt.enity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("SELECT p.namePermission FROM Permission p JOIN  p.roles r WHERE r.roleName=?1")
    List<String> findByRoleName(String roleName);

    Permission findByNamePermission(String namePermission);
}
