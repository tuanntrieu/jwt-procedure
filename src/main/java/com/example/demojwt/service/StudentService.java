package com.example.demojwt.service;

import com.example.demojwt.dto.request.StudentDeleteDto;
import com.example.demojwt.dto.request.StudentDto;
import com.example.demojwt.dto.request.StudentSearchDto;
import com.example.demojwt.dto.request.StudentUpdateDto;
import com.example.demojwt.dto.response.PageResponseDto;
import com.example.demojwt.dto.response.StudentResponseDto;
import com.example.demojwt.enity.Student;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    Student findById(Long id);

    void create(StudentDto studentDto);

    void update(StudentUpdateDto studentDto);

    void delete(StudentDeleteDto studentDto);

    PageResponseDto<StudentResponseDto> findStudents(StudentSearchDto studentSearchDto, Pageable pageable);


}
