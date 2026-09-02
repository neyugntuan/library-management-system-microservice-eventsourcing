package com.neyugntuan.employeeservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

@OpenAPIDefinition(
        info = @Info(
                title = "Employee Api Specification - Neyugntuan",
                description = "Api documentation for Employee Service",
                version = "1.0",
                contact = @Contact(
                        name = "Nguyen Thanh Tuan",
                        email = "neyugntuana9@gmail.com",
                        url = "https://github.com/neyugntuan"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://github.com/neyugntuan"
                ),
                termsOfService = "https://github.com/neyugntuan"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:9002"
                ),
                @Server(
                        description = "Dev ENV",
                        url = "https://employee-service.dev.com"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "https://employee-service.prod.com"
                )
        }
)
public class OpenApiConfig {

}
