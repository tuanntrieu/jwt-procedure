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
    @Query("SELECT p.namePermission FROM Permission p JOIN p.roles r  WHERE p.function.nameFunc = ?1 AND r.roleName = ?2")
    List<String> findPermissionNameByFunctionAndRole(String funcName, String roleName);

    @Query("SELECT DISTINCT p.permissionGr FROM Permission p")
    List<String> loadGroupPermission();

    @Query("SELECT p.namePermission FROM Permission p WHERE p.permissionGr=?1")
    List<String> findPermissionByGr(String permissionGr);

    Permission findByNamePermission(String namePermission);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM role_permission WHERE role_id=?1 AND permission_id=?2",nativeQuery = true)
    void deleteByRole(Long roleID,long permissionID);

    @Query("SELECT p.endPoint FROM Permission p JOIN p.roles r WHERE r.roleName=?1")
    List<String> allowedEndPoints(String roleName);

    @Query("SELECT p.namePermission FROM Role r JOIN r.permissions p WHERE r.roleName=?1")
    List<String> loadPermissionsByRole(String roleName);
}
