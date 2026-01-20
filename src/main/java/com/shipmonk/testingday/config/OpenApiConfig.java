package com.shipmonk.testingday.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation.
 * Accessible at http://localhost:8080/swagger-ui.html
 * API documentation at http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Exchange Rates API")
                .description("REST API for retrieving exchange rates from Fixer.io")
                .version("1.0.0")
                .contact(new Contact()
                    .name("PeterXMR")
                    .url("https://github.com/PeterXMR/exchange-rates-task-stub"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
