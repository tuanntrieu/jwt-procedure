package com.example.demojwt.service.impl;

import com.example.demojwt.dto.request.StudentDeleteDto;
import com.example.demojwt.dto.request.StudentDto;
import com.example.demojwt.dto.request.StudentSearchDto;
import com.example.demojwt.dto.request.StudentUpdateDto;
import com.example.demojwt.dto.response.PageResponseDto;
import com.example.demojwt.dto.response.StudentResponseDto;
import com.example.demojwt.enity.Student;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.repository.StudentRepository;
import com.example.demojwt.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public Student findById(Long id) {
        Student student= studentRepository.getStudentById(id);
        if(student == null) {
            throw new NotFoundException("Student not found");
        }
        return student;
    }

    @Override
    public void create(StudentDto studentDto) {
        studentRepository.addStudent(studentDto);
    }

    @Override
    public void update(StudentUpdateDto studentDto) {
        Student student=studentRepository.getStudentById(studentDto.getId());
        if(student == null) {
            throw new NotFoundException("Student not found");
        }
        studentRepository.updateStudent(studentDto);

    }

    @Override
    public void delete(StudentDeleteDto studentDto) {
        Student student=studentRepository.getStudentById(studentDto.getId());
        if(student == null) {
            throw new NotFoundException("Student not found");
        }
        studentRepository.deleteStudentById(studentDto.getId());
    }

    @Override
    public PageResponseDto<StudentResponseDto> findStudents(StudentSearchDto studentSearchDto, Pageable pageable) {
        Page<StudentResponseDto> page=studentRepository.searchStudent(studentSearchDto, pageable);
        PageResponseDto responseDto=new PageResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setTotalElements(page.getTotalElements());
        responseDto.setTotalPages(page.getTotalPages());
        responseDto.setPageNo(page.getNumber());
        responseDto.setPageSize(page.getSize());
        responseDto.setSort(page.getSort().toString());
        return responseDto;
    }


}