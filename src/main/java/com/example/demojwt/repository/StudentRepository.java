package com.example.demojwt.repository;





import com.example.demojwt.dto.request.StudentDto;
import com.example.demojwt.dto.request.StudentSearchDto;
import com.example.demojwt.dto.request.StudentUpdateDto;
import com.example.demojwt.enity.Student;
import jakarta.persistence.*;

import jakarta.transaction.Transactional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.sql.Date;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


@Component
public class StudentRepository {
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
            student.setName((String) result[4]);
            student.setAddress((String) result[1]);
            student.setGender((String) result[3]);
            Timestamp timestamp = (Timestamp) result[2];
            student.setBirthday(new Date(timestamp.getTime()));
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

        query.setParameter("s_name", studentDto.getName());
        query.setParameter("s_address", studentDto.getAddress());
        query.setParameter("s_gender", studentDto.getGender());
        query.setParameter("s_birthday", studentDto.getBirthday());

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
    public Page<Student> searchStudent(StudentSearchDto studentSearchDto, Pageable pageable) {
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
        List<Student> students = new ArrayList<>();


        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            Student student = new Student();
            student.setId((Long) result[0]);
            student.setName((String) result[4]);
            student.setAddress((String) result[1]);
            student.setGender((String) result[3]);
            Timestamp timestamp = (Timestamp) result[2];
            student.setBirthday(new Date(timestamp.getTime()));
            students.add(student);
        }
        Long total = (Long) query.getOutputParameterValue("total");

        return new PageImpl<>(students, pageable, total);
    }


}