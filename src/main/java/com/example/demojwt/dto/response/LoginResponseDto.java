package com.example.demojwt.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponseDto {
    private String tokenType;

    private String username;

    private String accessToken;

    public static LoginResponseDtoBuilder builder() {
        return new LoginResponseDtoBuilder().tokenType("Bearer");
    }
}
