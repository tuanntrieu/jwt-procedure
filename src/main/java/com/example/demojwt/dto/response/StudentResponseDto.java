package com.example.demojwt.dto.response;

import com.example.demojwt.enity.Permission;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class StudentResponseDto {
    private Long id;

    private String name;

    private String address;

    private Date birthday;

    private String gender;

    private String userName;

    private String role;

    private List<String> permissions;
}