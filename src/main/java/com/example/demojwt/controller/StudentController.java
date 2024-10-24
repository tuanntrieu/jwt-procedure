package com.example.demojwt.controller;

import com.example.demojwt.base.VsResponseUtil;
import com.example.demojwt.dto.request.*;
import com.example.demojwt.service.RoleService;
import com.example.demojwt.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final RoleService roleService;

    @PostMapping("/create-student")
    public ResponseEntity<?> createStudent(@Validated @RequestBody StudentDto studentDto) {
        studentService.create(studentDto);
        return VsResponseUtil.success("Student is created");
    }

    @PostMapping("/update-student")
    public ResponseEntity<?> updateStudent(@Validated @RequestBody StudentUpdateDto studentDto) {
        studentService.update(studentDto);
        return VsResponseUtil.success("Student is updated");
    }

    @PostMapping("/delete-student")
    public ResponseEntity<?> deleteStudent(@RequestBody StudentDeleteDto studentDto) {
        studentService.delete(studentDto);
        return VsResponseUtil.success("Successfully deleted student");
    }

    @PostMapping("/search-student")
    public ResponseEntity<?> searchStudents(@RequestBody StudentSearchDto dto) {
        return VsResponseUtil.success(studentService.findStudents(dto, PageRequest.of(dto.getPageNo(), dto.getPageSize(), Sort.by(dto.getSortBy()))));
    }

    @PostMapping("/update-role-student")
    public ResponseEntity<?> updateRole(@RequestBody StudentUpdateRoleDto studentUpdateRoleDto) {
        roleService.updateRole(studentUpdateRoleDto);
        return VsResponseUtil.success("Role updated");
    }

    @PostMapping(value = "/export-student-excel")
    public void exportExcelStudent(@RequestBody StudentSearchExportDto studentUpdateRoleDto, HttpServletResponse response) throws IOException {
        studentService.exportStudents(studentUpdateRoleDto, response);
    }

    @PostMapping("/import/create-example-file")
    public void createEx(HttpServletResponse response) throws IOException {
        studentService.createExampleFile(response);
    }

    @PostMapping("/import/import-student")
    public ResponseEntity<?> importStudent(@ModelAttribute ImportStudentDto dto) throws IOException {
        studentService.importStudent(dto);
        return VsResponseUtil.success("Successfully imported student");
    }

}
