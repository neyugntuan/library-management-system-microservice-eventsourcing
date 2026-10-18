package com.neyugntuan.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateUserRequestDTO {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String name;

    private String password;
}