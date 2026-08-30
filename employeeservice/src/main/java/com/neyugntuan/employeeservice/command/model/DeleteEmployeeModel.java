package com.neyugntuan.employeeservice.command.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteEmployeeModel {
    @NotBlank(message = "Id is mandadory")
    private String id;

}
