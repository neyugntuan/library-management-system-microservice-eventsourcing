package com.neyugntuan.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDto {
    @NotBlank(message = "Username is mandatory!")
    String username;
    @NotBlank(message = "Password is mandatory!")
    String password;
}
