package com.refresh.javatask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .version("1.0.0")
                        .description("API for managing tasks")
                        .contact(new Contact().name("Farhan Maulana Yasykur")
                                .url("https://github.com/Kurs24/task-manager.git"))
                ).servers(
                        List.of(new Server().url("http://localhost:8080").description("Localhost"))
                );
    }
}