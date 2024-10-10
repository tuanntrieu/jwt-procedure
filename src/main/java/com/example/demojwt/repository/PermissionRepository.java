package com.example.demojwt.repository;

import com.example.demojwt.enity.Permission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("SELECT p.namePermission FROM Permission p JOIN  p.roles r WHERE r.roleName=?1")
    List<String> findByRoleName(String roleName);

    Permission findByNamePermission(String namePermission);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM role_permission WHERE role_id=?1 AND permission_id=?2",nativeQuery = true)
    void deleteByRoleName(Long roleID,long permissionID);

    @Query("SELECT p.url FROM Permission p JOIN p.roles r WHERE r.roleName=?1")
    List<String> allowedUrls(String roleName);

    @Query("SELECT p FROM Permission p")
    List<Permission> loadAllPermissions();
}
