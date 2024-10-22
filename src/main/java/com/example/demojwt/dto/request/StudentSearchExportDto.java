package com.example.demojwt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchExportDto {
    private String name;
    private String gender;
    private String address;
    private Date startDate;
    private Date endDate;
}
