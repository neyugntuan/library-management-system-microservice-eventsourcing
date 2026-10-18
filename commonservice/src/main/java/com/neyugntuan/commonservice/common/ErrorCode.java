package com.neyugntuan.commonservice.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {
    USER_EXISTED(1002, "User existed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be atleast {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be atleast {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND), // 404
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED), // 401 chưa xác thực
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN), // 403 không có quyền truy cập tài nguyên
    EMPLOYEE_NOT_EXISTED(1008, "Employee not existed", HttpStatus.NOT_FOUND),
    BOOK_NOT_EXISTED(1009,"BOOK NOT EXISTED", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
