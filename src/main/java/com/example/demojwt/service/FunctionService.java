package com.example.demojwt.service;

import com.example.demojwt.dto.response.FunctionResponseDto;
import com.example.demojwt.enity.Function;

import java.util.List;

public interface FunctionService {

    List<Function> getFunctionByRoleName(String roleName);

    List<FunctionResponseDto> loadFunctionResponseByRole(String role);


}
