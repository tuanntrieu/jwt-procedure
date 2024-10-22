package com.example.demojwt.service.impl;

import com.example.demojwt.dto.response.FunctionResponseDto;
import com.example.demojwt.enity.Function;
import com.example.demojwt.repository.FunctionRepository;
import com.example.demojwt.repository.PermissionRepository;
import com.example.demojwt.service.FunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionServiceImpl implements FunctionService {
    private final FunctionRepository functionRepository;
    private final PermissionRepository permissionRepository;


    @Override
    public List<Function> getFunctionByRoleName(String roleName) {
        return functionRepository.findFunctionByRoleName(roleName);
    }

    @Override
    public List<FunctionResponseDto> loadFunctionResponseByRole(String role) {
        List<FunctionResponseDto> funcs = new ArrayList<>();

        functionRepository.findFunctionByRoleName(role).forEach(func -> {
            FunctionResponseDto funcDto = new FunctionResponseDto();
            funcDto.setFunctionName(func.getNameFunc());
            funcDto.setPermissions(permissionRepository.findPermissionNameByFunctionAndRole(func.getNameFunc(), role));
            funcs.add(funcDto);
        });
        return funcs;
    }


}
