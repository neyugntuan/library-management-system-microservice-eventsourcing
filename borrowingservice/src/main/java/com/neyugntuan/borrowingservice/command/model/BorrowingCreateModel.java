package com.neyugntuan.borrowingservice.command.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingCreateModel {
    @NotBlank(message = "Book Id is mandatory")
    private String bookId;
    @NotBlank(message = "Employee Id is mandatory")
    private String employeeId;
}
