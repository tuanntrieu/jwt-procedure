package com.example.demojwt.service;

import com.example.demojwt.dto.request.*;
import com.example.demojwt.dto.response.PageResponseDto;
import com.example.demojwt.dto.response.StudentResponseDto;
import com.example.demojwt.enity.Student;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface StudentService {

    Student findById(Long id);

    void create(StudentDto studentDto);

    void update(StudentUpdateDto studentDto);

    void delete(StudentDeleteDto studentDto);

    PageResponseDto<StudentResponseDto> findStudents(StudentSearchDto studentSearchDto, Pageable pageable);

    void createExampleFile(HttpServletResponse response) throws IOException;

    void exportStudents(StudentSearchExportDto studentSearchDto, HttpServletResponse response) throws IOException;

    void importStudent(ImportStudentDto importStudentDto) throws IOException;;
}
