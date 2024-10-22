package com.example.demojwt.repository;

import com.example.demojwt.enity.Function;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM role_function WHERE role_id=?1 AND function_id=?2",nativeQuery = true)
    void deleteByRole(Long roleID,long functionID);

    @Query("SELECT f.url FROM Function f JOIN  f.roles r WHERE r.roleName=?1")
    List<String> loadFunctionUrlByRoleName(String roleName);


    @Query("SELECT f FROM Function f")
    List<Function> loadAllFunctions();

    @Query("SELECT r.functions FROM Role r WHERE r.roleName=?1")
    List<Function> findFunctionByRoleName(String roleName);


}
