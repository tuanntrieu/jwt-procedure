package com.example.demojwt.controller;

import com.example.demojwt.base.VsResponseUtil;
import com.example.demojwt.constant.PermissionConstant;
import com.example.demojwt.constant.RoleConstant;
import com.example.demojwt.dto.request.StudentDeleteDto;
import com.example.demojwt.dto.request.StudentDto;
import com.example.demojwt.dto.request.StudentSearchDto;
import com.example.demojwt.dto.request.StudentUpdateDto;

import com.example.demojwt.enity.Permission;
import com.example.demojwt.enity.Role;
import com.example.demojwt.repository.PermissionRepository;
import com.example.demojwt.repository.RoleRepository;
import com.example.demojwt.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    @PostMapping("/create-student")
    public ResponseEntity<?> createStudent(@Validated @RequestBody StudentDto studentDto){
        studentService.create(studentDto);
        return VsResponseUtil.success("Student is created");
    }
    @PostMapping("/update-student")
    public ResponseEntity<?> updateStudent(@Validated @RequestBody StudentUpdateDto studentDto){
        studentService.update(studentDto);
        return VsResponseUtil.success("Student is updated");
    }

    @PostMapping("/delete-student")
    public ResponseEntity<?> deleteStudent(@RequestBody StudentDeleteDto studentDto){
        studentService.delete(studentDto);
        return VsResponseUtil.success("Successfully deleted student");
    }

    @PostMapping("/search-student")
    public ResponseEntity<?> searchStudents(@RequestBody StudentSearchDto dto) {
        return VsResponseUtil.success(studentService.findStudents(dto, PageRequest.of(dto.getPageNo(), dto.getPageSize(), Sort.by(dto.getSortBy()))));
    }

}
