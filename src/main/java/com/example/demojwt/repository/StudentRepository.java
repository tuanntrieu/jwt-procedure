package com.example.demojwt.repository;





import com.example.demojwt.constant.RoleConstant;
import com.example.demojwt.dto.request.StudentDto;
import com.example.demojwt.dto.request.StudentSearchDto;
import com.example.demojwt.dto.request.StudentUpdateDto;
import com.example.demojwt.dto.response.StudentResponseDto;
import com.example.demojwt.enity.Role;
import com.example.demojwt.enity.Student;
import com.example.demojwt.enity.User;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.service.RoleService;
import jakarta.persistence.*;

import jakarta.transaction.Transactional;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


import java.sql.Date;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class StudentRepository {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Student getStudentById(Long id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("STUDENT_PKG.findStudentById");
        query.registerStoredProcedureParameter("s_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("c_student", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("s_id", id);
        query.execute();
        List<Object[]> results = query.getResultList();
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            Student student = new Student();
            student.setId((Long) result[0]);
            student.setName((String) result[1]);
            student.setAddress((String) result[2]);
            student.setGender((String) result[4]);
            Timestamp timestamp = (Timestamp) result[3];
            student.setBirthday(new Date(timestamp.getTime()));
            User user=userRepository.findById((Long)result[5]).orElseThrow(
                    () -> new NotFoundException("User Not Found")
            );
            student.setUser(user);
            return student;
        }
        return null;
    }

    @Transactional
    public void addStudent(StudentDto studentDto) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("STUDENT_PKG.addStudent");
        query.registerStoredProcedureParameter("s_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_birthday", Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_gender", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("username", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("password", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("role_id", Long.class, ParameterMode.IN);

        query.setParameter("s_name", studentDto.getName());
        query.setParameter("s_address", studentDto.getAddress());
        query.setParameter("s_gender", studentDto.getGender());
        query.setParameter("s_birthday", studentDto.getBirthday());
        query.setParameter("username", studentDto.getUsername());
        query.setParameter("password", passwordEncoder.encode(studentDto.getPassword()));
        Role role=roleRepository.findByRoleName(RoleConstant.STUDENT).orElseThrow(
                ()->new NotFoundException("Role not found")
        );
        query.setParameter("role_id",role.getId());

        query.execute();
    }

    @Transactional
    public void updateStudent(StudentUpdateDto studentDto) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("STUDENT_PKG.updateStudent");
        query.registerStoredProcedureParameter("s_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_birthday", Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_gender", String.class, ParameterMode.IN);

        query.setParameter("s_id", studentDto.getId());
        query.setParameter("s_name", studentDto.getName());
        query.setParameter("s_address", studentDto.getAddress());
        query.setParameter("s_gender", studentDto.getGender());
        query.setParameter("s_birthday", studentDto.getBirthday());
        query.execute();
    }


    @Transactional
    public void deleteStudentById(Long id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("STUDENT_PKG.deleteStudent");
        query.registerStoredProcedureParameter("s_id", Long.class, ParameterMode.IN);
        query.setParameter("s_id", id);
        query.execute();
    }

    @Transactional
    public Page<StudentResponseDto> searchStudent(StudentSearchDto studentSearchDto, Pageable pageable) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("STUDENT_PKG.searchStudent");
        query.registerStoredProcedureParameter("s_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_address", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_gender", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_start", Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("s_end", Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pageNo", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pageSize", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("sortBy", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("c_students", void.class, ParameterMode.REF_CURSOR);
        query.registerStoredProcedureParameter("total", Long.class, ParameterMode.OUT);

        query.setParameter("s_name", studentSearchDto.getName());
        query.setParameter("s_address", studentSearchDto.getAddress());
        query.setParameter("s_gender", studentSearchDto.getGender());
        query.setParameter("s_start", studentSearchDto.getStartDate());
        query.setParameter("s_end", studentSearchDto.getEndDate());
        query.setParameter("pageNo", pageable.getPageNumber());
        query.setParameter("pageSize", pageable.getPageSize());
        query.setParameter("sortBy", studentSearchDto.getSortBy());

        query.execute();
        List<StudentResponseDto> students = new ArrayList<>();


        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Timestamp timestamp = (Timestamp) result[4];
            StudentResponseDto student = StudentResponseDto.builder()
                    .id((Long) result[0])
                    .name((String) result[1])
                    .gender((String) result[2])
                    .address((String) result[3])
                    .birthday((new Date(timestamp.getTime())))
                    .userName((String) result[5])
                    .role((String) result[6])
                    .permissions(permissionRepository.findByRoleName(result[6].toString()))
                    .build();
            students.add(student);
        }
        Long total = (Long) query.getOutputParameterValue("total");

        return new PageImpl<>(students, pageable, total);
    }


}