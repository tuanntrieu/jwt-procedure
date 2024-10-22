package com.example.demojwt.dto.response;

import com.example.demojwt.enity.Permission;
import lombok.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

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

 //   private List<FunctionResponseDto> functions=new ArrayList<>();
}
