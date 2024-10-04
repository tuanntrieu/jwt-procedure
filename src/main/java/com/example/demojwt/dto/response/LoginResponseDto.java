package com.example.demojwt.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LoginResponseDto {
    private String tokenType;

    private String username;

    private String accessToken;

    private String role;

    private List<String> permissions;

    public static LoginResponseDtoBuilder builder() {
        return new LoginResponseDtoBuilder().tokenType("Bearer");
    }
}
