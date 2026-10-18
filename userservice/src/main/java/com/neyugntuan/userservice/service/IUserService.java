package com.neyugntuan.userservice.service;

import com.neyugntuan.userservice.dto.CreateUserRequestDTO;
import com.neyugntuan.userservice.dto.LoginRequestDto;
import com.neyugntuan.userservice.dto.UpdateUserRequestDTO;
import com.neyugntuan.userservice.dto.UserResponseDTO;
import com.neyugntuan.userservice.dto.identity.TokenExchangeResponse;

import java.util.List;

public interface IUserService {
    UserResponseDTO createUser(CreateUserRequestDTO dto);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, UpdateUserRequestDTO dto, String token);
    void deleteUser(Long id, String token);

    TokenExchangeResponse login(LoginRequestDto dto);
}
