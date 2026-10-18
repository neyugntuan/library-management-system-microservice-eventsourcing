package com.neyugntuan.userservice.config;


import com.neyugntuan.userservice.dto.CreateUserRequestDTO;
import com.neyugntuan.userservice.dto.identity.TokenExchangeParam;
import com.neyugntuan.userservice.repository.IdentityClient;
import com.neyugntuan.userservice.repository.UserRepository;
import com.neyugntuan.userservice.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    @NonFinal
    static final String ADMIN_EMAIL = "admin@gmail.com";

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;


    @Bean
    ApplicationRunner applicationRunner(IUserService userService, IdentityClient identityClient){
        return args -> {
            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());
            if(identityClient.findUsersByEmail("Bearer " + token.getAccessToken(), ADMIN_EMAIL, true).isEmpty()){

                CreateUserRequestDTO admin = CreateUserRequestDTO.builder()
                        .email(ADMIN_EMAIL)
                        .username(ADMIN_USER_NAME)
                        .firstName("admin")
                        .lastName("admin")
                        .dob(LocalDate.parse("1990-01-01"))
                        .name("admin")
                        .password(ADMIN_PASSWORD)
                        .build();

                userService.createUser(admin);
            }
            log.warn("admin user has been created with defauft password: admin, please change it");
        };
    }
}
