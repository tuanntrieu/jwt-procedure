package com.example.demojwt.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {
    private Long id;

    private String name;

    private String address;

    private Date birthday;

    private String gender;

    private String userName;

    private String role;

    private String status;

    private String rejectReason;
}
