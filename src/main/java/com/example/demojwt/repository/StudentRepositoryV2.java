package com.example.demojwt.repository;

import com.example.demojwt.constant.RoleConstant;
import com.example.demojwt.dto.request.StudentDto;
import com.example.demojwt.dto.request.StudentSearchDto;
import com.example.demojwt.dto.request.StudentUpdateDto;
import com.example.demojwt.dto.response.StudentResponseDto;
import com.example.demojwt.enity.Role;
import com.example.demojwt.enity.Student;
import com.example.demojwt.exception.NotFoundException;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Component
@RequiredArgsConstructor
public class StudentRepositoryV2  {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Student getStudentById(Long id) {
        try {
            Query query = entityManager.createNativeQuery("CALL findStudentById(:s_id)", Student.class);
            query.setParameter("s_id", id);
            return (Student) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    @Transactional
    public void addStudent(StudentDto studentDto) {
        String encodedPassword = passwordEncoder.encode(studentDto.getPassword());
        try {
            Query query = entityManager.createNativeQuery("CALL addStudent(:s_name, :s_address, :s_birthday, :s_gender, :username, :password, :role_id)");
            query.setParameter("s_name", studentDto.getName());
            query.setParameter("s_address", studentDto.getAddress());
            query.setParameter("s_birthday", studentDto.getBirthday());
            query.setParameter("s_gender", studentDto.getGender());
            query.setParameter("username", studentDto.getUsername());
            query.setParameter("password", encodedPassword);
            Role role= roleRepository.findByRoleName(RoleConstant.STUDENT)
                    .orElseThrow(() -> new NotFoundException("Role not found"));
            query.setParameter("role_id", role.getId());
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public void updateStudent(StudentUpdateDto studentDto) {
        try {
            Query query = entityManager.createNativeQuery("CALL updateStudent(:s_id, :s_name, :s_address, :s_birthday, :s_gender)");
            query.setParameter("s_id", studentDto.getId());
            query.setParameter("s_name", studentDto.getName());
            query.setParameter("s_address", studentDto.getAddress());
            query.setParameter("s_birthday", studentDto.getBirthday());
            query.setParameter("s_gender", studentDto.getGender());
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public void deleteStudentById(Long id) {
        try {
            Query query = entityManager.createNativeQuery("CALL deleteStudent(:s_id)");
            query.setParameter("s_id", id);
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public Page<StudentResponseDto> searchStudent(StudentSearchDto studentSearchDto, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT s FROM Student s WHERE 1=1");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(s) FROM Student s WHERE 1=1");
        StringBuilder where = new StringBuilder();

        Map<String, Object> params = new HashMap<>();

        if (studentSearchDto.getName() != null && !studentSearchDto.getName().isEmpty()) {
            where.append(" AND LOWER(s.name) LIKE :name");
            params.put("name", "%" + studentSearchDto.getName().trim().toLowerCase() + "%");
        }
        if (studentSearchDto.getGender() != null && !studentSearchDto.getGender().isEmpty()) {
            where.append(" AND LOWER(s.gender) = :gender");
            params.put("gender", studentSearchDto.getGender().trim().toLowerCase());
        }
        if (studentSearchDto.getAddress() != null && !studentSearchDto.getAddress().isEmpty()) {
            where.append(" AND LOWER(s.address) LIKE :address");
            params.put("address", "%" + studentSearchDto.getAddress().trim().toLowerCase() + "%");
        }
        if (studentSearchDto.getStartDate() != null) {
            where.append(" AND s.birthday >= :startDate");
            params.put("startDate", studentSearchDto.getStartDate());
        }
        if (studentSearchDto.getEndDate() != null) {
            where.append(" AND s.birthday <= :endDate");
            params.put("endDate", studentSearchDto.getEndDate());
        }

        String sortBy = studentSearchDto.getSortBy();
        if (sortBy != null && !sortBy.isEmpty()) {
            where.append(" ORDER BY s.").append(sortBy);
        }

        sql.append(where);
        sqlCount.append(where);

        TypedQuery<Student> query = entityManager.createQuery(sql.toString(), Student.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Student> students = query.getResultList();
        List<StudentResponseDto> response=new ArrayList<>();
        students.forEach(student -> {
            StudentResponseDto studentResponseDto = new StudentResponseDto();
            studentResponseDto.setId(student.getId());
            studentResponseDto.setName(student.getName());
            studentResponseDto.setAddress(student.getAddress());
            studentResponseDto.setBirthday(student.getBirthday());
            studentResponseDto.setGender(student.getGender());
            studentResponseDto.setUserName(student.getUser().getUsername());
            studentResponseDto.setRole(student.getUser().getRole().getRoleName());
            List<String> permissions = permissionRepository.findByRoleName(student.getUser().getRole().getRoleName());
            studentResponseDto.setPermissions(permissions);
            response.add(studentResponseDto);
        });
        TypedQuery<Long> countQuery = entityManager.createQuery(sqlCount.toString(), Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = countQuery.getSingleResult();


        return new PageImpl<>(response, pageable, count);
    }



}
