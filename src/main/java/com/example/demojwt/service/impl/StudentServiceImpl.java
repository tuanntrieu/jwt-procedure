package com.example.demojwt.service.impl;

import com.example.demojwt.dto.request.*;
import com.example.demojwt.dto.response.PageResponseDto;
import com.example.demojwt.dto.response.StudentResponseDto;
import com.example.demojwt.enity.Student;
import com.example.demojwt.exception.DataIntegrityViolationException;
import com.example.demojwt.exception.InvalidException;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.repository.StudentRepositoryV2;
import com.example.demojwt.repository.UserRepository;
import com.example.demojwt.service.StudentService;
import com.example.demojwt.service.UserService;
import com.example.demojwt.util.ExcelExportUtil;
import com.example.demojwt.util.ExcelImportUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepositoryV2 studentRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ExcelImportUtil importUtil;

    @Override
    public Student findById(Long id) {
        Student student = studentRepository.getStudentById(id);
        if (student == null) {
            throw new NotFoundException("Student not found");
        }
        return student;
    }

    @Override
    public void create(StudentDto studentDto) {
        if (userService.existsByUsername(studentDto.getUsername())) {
            throw new DataIntegrityViolationException("Username is already taken");
        }
        if (!studentDto.getPassword().equals(studentDto.getRePassword())) {
            throw new DataIntegrityViolationException("Password is not match");
        }
        studentRepository.addStudent(studentDto);
    }

    @Override
    public void update(StudentUpdateDto studentDto) {
        Student student = studentRepository.getStudentById(studentDto.getId());
        if (student == null) {
            throw new NotFoundException("Student not found");
        }
        studentRepository.updateStudent(studentDto);

    }

    @Override
    public void delete(StudentDeleteDto studentDto) {
        Student student = studentRepository.getStudentById(studentDto.getId());
        if (student == null) {
            throw new NotFoundException("Student not found");
        }
        studentRepository.deleteStudentById(studentDto.getId());
        userRepository.delete(student.getUser());

    }

    @Override
    public PageResponseDto<StudentResponseDto> findStudents(StudentSearchDto studentSearchDto, Pageable pageable) {
        Page<StudentResponseDto> page = studentRepository.searchStudent(studentSearchDto, pageable);
        PageResponseDto responseDto = new PageResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setTotalElements(page.getTotalElements());
        responseDto.setTotalPages(page.getTotalPages());
        responseDto.setPageNo(page.getNumber());
        responseDto.setPageSize(page.getSize());
        responseDto.setSort(page.getSort().toString());
        return responseDto;
    }

    @Override
    public void createExampleFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=example.xlsx";
        response.setHeader(headerKey, headerValue);

        importUtil.createExampleExcelFile(response);
    }

    @Override
    public void exportStudents(StudentSearchExportDto studentSearchDto, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=students_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<StudentResponseDto> list = studentRepository.searchExport(studentSearchDto);
        ExcelExportUtil exportUtil = new ExcelExportUtil(list);
        exportUtil.exportDataToExcel(response);
    }



    @Override
    public void importStudent(ImportStudentDto importStudentDto) throws IOException{
        try {
            if (!ExcelImportUtil.isExcelFile(importStudentDto.getFile())) {
                throw new InvalidException("Incorrect file type");
            }
            List<StudentDto> studentDtos = importUtil.extractFromFile(importStudentDto.getFile().getInputStream());
            studentDtos.forEach(student -> {
                studentRepository.addStudent(student);
            });
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(2, e);
        }
    }


}