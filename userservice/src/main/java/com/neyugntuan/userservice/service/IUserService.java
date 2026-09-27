package com.neyugntuan.userservice.service;

import com.neyugntuan.userservice.dto.CreateUserRequestDTO;
import com.neyugntuan.userservice.dto.LoginRequestDto;
import com.neyugntuan.userservice.dto.UserResponseDTO;
import com.neyugntuan.userservice.dto.identity.TokenExchangeResponse;

import java.util.List;

public interface IUserService {
    UserResponseDTO createUser(CreateUserRequestDTO dto);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, CreateUserRequestDTO dto);
    void deleteUser(Long id);

    TokenExchangeResponse login(LoginRequestDto dto);
}
