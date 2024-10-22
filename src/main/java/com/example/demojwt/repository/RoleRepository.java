package com.example.demojwt.repository;

import com.example.demojwt.enity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);

    @Query("SELECT r.roleName FROM Role r WHERE r.id<>1")
    List<String> findAllRoleName();

    boolean existsByRoleName(String roleName);

    @Query("DELETE FROM Role r WHERE r.roleName=?1 ")
    @Modifying
    @Transactional
    void deleteByRoleName(String roleName);

    @Query(value = "DELETE FROM role_function WHERE role_id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteRoleInFunction(Long id);

    @Query(value = "DELETE FROM role_permission WHERE role_id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteRoleInPer(Long id);
}
