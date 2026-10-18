package com.neyugntuan.userservice.repository;
import com.neyugntuan.userservice.dto.identity.TokenExchangeParam;
import com.neyugntuan.userservice.dto.identity.TokenExchangeResponse;
import com.neyugntuan.userservice.dto.identity.UserCreationParam;
import com.neyugntuan.userservice.dto.identity.UserTokenExchangeParam;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {

    @PostMapping(
            value = "/realms/neyugntuan/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

    @PostMapping(value = "/admin/realms/neyugntuan/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(@RequestHeader("authorization") String token, @RequestBody UserCreationParam param);

    @PostMapping(value = "realms/neyugntuan/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeUserToken(@QueryMap UserTokenExchangeParam param);

    @GetMapping("/admin/realms/neyugntuan/users")
    List<Map<String, Object>> findUsersByEmail(
            @RequestHeader("authorization") String token,
            @RequestParam("email") String email,
            @RequestParam("exact") boolean exact
    );

    @PutMapping(
            value = "/admin/realms/neyugntuan/users/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> updateUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("userId") String userId,
            @RequestBody UserCreationParam param
    );

    @DeleteMapping("/admin/realms/neyugntuan/users/{userId}")
    ResponseEntity<Void> deleteUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("userId") String userId
    );

}
