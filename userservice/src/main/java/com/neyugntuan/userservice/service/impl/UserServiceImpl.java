package com.neyugntuan.userservice.service.impl;

import com.neyugntuan.commonservice.common.AppException;
import com.neyugntuan.commonservice.common.ErrorCode;
import com.neyugntuan.commonservice.services.KafkaService;
import com.neyugntuan.userservice.dto.CreateUserRequestDTO;
import com.neyugntuan.userservice.dto.LoginRequestDto;
import com.neyugntuan.userservice.dto.UpdateUserRequestDTO;
import com.neyugntuan.userservice.dto.UserResponseDTO;
import com.neyugntuan.userservice.dto.identity.*;
import com.neyugntuan.userservice.dto.identity.*;
import com.neyugntuan.userservice.entity.User;
import com.neyugntuan.userservice.repository.IdentityClient;
import com.neyugntuan.userservice.repository.UserRepository;

import com.neyugntuan.userservice.service.IUserService;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdentityClient identityClient;

    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    @Override
    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        // Exchange client Token
        var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                .grant_type("client_credentials")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .build());

        log.info("TokenInfo {}", token);

        // Get userId of keyCloak account
        var creationResponse = identityClient.createUser(
                "Bearer " + token.getAccessToken(),
                UserCreationParam.builder()
                        .username(request.getUsername())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .enabled(true)
                        .emailVerified(false)
                        .credentials(List.of(Credential.builder()
                                .type("password")
                                .temporary(false)
                                .value(request.getPassword())
                                .build()))
                        .build());

        String userId = extractUserId(creationResponse);
        log.info("UserId {}", userId);
        User user = userRepository.save(toEntity(request,userId));

        kafkaService.sendMessage("emailTemplate", user.getEmail());
        return toDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return toDTO(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO dto, String token) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_EXISTED));

        String accessToken = token.startsWith("Bearer ")
                ? token
                : "Bearer " + token;


        // Chuẩn bị dữ liệu update Keycloak
        UserCreationParam keycloakUser =
                UserCreationParam.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .build();

        identityClient.updateUser(
                accessToken,
                user.getUserId(),
                keycloakUser
        );

        // Update MySQL
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDob(dto.getDob());
        user.setName(dto.getName());

        return toDTO(userRepository.save(user));
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id, String token) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_EXISTED));

        String accessToken = token.startsWith("Bearer ")
                ? token
                : "Bearer " + token;


        identityClient.deleteUser(accessToken, user.getUserId());

        userRepository.deleteById(id);
    }

    @Override
    public TokenExchangeResponse login(LoginRequestDto dto) {
        var token = identityClient.exchangeUserToken(UserTokenExchangeParam.builder()
                .grant_type("password")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build());
        return token;
    }

    private UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(user.getDob())
                .name(user.getName())
                .id(user.getId())
                .build();
    }

    private User toEntity(CreateUserRequestDTO request, String userId) {
        return User.builder()
                .userId(userId)
                .email(request.getEmail())
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .name(request.getName())
                .build();
    }

    private String extractUserId(ResponseEntity<?> response) {
        List<String> locations = response.getHeaders().get("Location");
        if (locations == null || locations.isEmpty()) {
            throw new IllegalStateException("Location header is missing in the response");
        }

        String location = locations.get(0);
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }
}
