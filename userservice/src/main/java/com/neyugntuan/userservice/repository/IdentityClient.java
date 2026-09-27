package com.neyugntuan.userservice.repository;
import com.neyugntuan.userservice.dto.identity.TokenExchangeParam;
import com.neyugntuan.userservice.dto.identity.TokenExchangeResponse;
import com.neyugntuan.userservice.dto.identity.UserCreationParam;
import com.neyugntuan.userservice.dto.identity.UserTokenExchangeParam;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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
}
