package com.neyugntuan.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateUserRequestDTO {
    @NotBlank(message = "Email is mandatory!")
    private String email;
    @NotBlank(message = "Username is mandatory!")
    private String username;
    @NotBlank(message = "First name is mandatory!")
    private String firstName;
    @NotBlank(message = "Last name is mandatory!")
    private String lastName;
    @NotNull(message = "Date of birth is mandatory!")
    private LocalDate dob;
    @NotBlank(message = "Name is mandatory!")
    private String name;
    @NotBlank(message = "Password is mandatory!")
    private String password;
}
