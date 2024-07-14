package com.written.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Taewoo",
                        email = "taewookim02@gmail.com",
                        url = "https://github.com/taewookim02"
                ),
                description = "OpenApi documentation for Written Thoughts",
                title = "OpenApi specifiaction - Written Thoughts",
                version = "1.0",
                /*license = @License(
                        name = ""
                        url = ""
                )*/
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/api/v1"
                ),
                @Server(
                        description = "Local ENV2",
                        url = "http://127.0.0.1:8080/api/v1"
                )
                /*@Server(
                        description = "Prod ENV2",
                        url = "https://writtenthoughts.com"
                )*/
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
// TODO: implement jwt
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
