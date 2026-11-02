package com.refresh.javatask.config;

import com.refresh.javatask.responsedto.APIError;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiErrorResponseConfig {

    static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    static final String BAD_REQUEST = "Bad Request";
    static final String NOT_FOUND = "Not Found";

    @Bean
    public OpenApiCustomizer openApiCustomizer() {

        ResolvedSchema errSchema =
                ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(APIError.class));

        return openApi -> {
            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }
            errSchema.referencedSchemas.forEach(openApi.getComponents()::addSchemas);
            openApi.getPaths()
                    .forEach((path, item) -> item.readOperations().forEach(operation -> {
                                ApiResponses response = operation.getResponses();
                                addErrorIfNotExists(response, "500", INTERNAL_SERVER_ERROR,
                                        errorContent(500, INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, "/api/tasks",
                                                false));
                                addErrorIfNotExists(response, "400", BAD_REQUEST,
                                        errorContent(400, BAD_REQUEST, "Validation Error", "/api/tasks", true));
                                if (path.contains("{")) {
                                    addErrorIfNotExists(response, "404", NOT_FOUND,
                                            errorContent(404, NOT_FOUND, "Task with id 99999 not found", "/api/tasks/99999",
                                                    false));
                                }
                            }
                    ));
        };
    }

    private void addErrorIfNotExists(ApiResponses response, String code, String description, Content content) {
        if (!response.containsKey(code)) {
            response.addApiResponse(code, new ApiResponse().description(description).content(content));
        }
    }

    private Content errorContent(int status, String error, String message, String path, boolean withViolations) {
        Map<String, Object> errorsExample = new LinkedHashMap<>();
        errorsExample.put("timestamp", "2026-10-31T04:26:52.281Z");
        errorsExample.put("status", status);
        errorsExample.put("error", error);
        errorsExample.put("message", message);
        errorsExample.put("path", path);
        if (withViolations) {
            Map<String, String> violations = new LinkedHashMap<>();
            violations.put("field", "title");
            violations.put("message", "Title is required");
            errorsExample.put("fieldViolations", violations);
        }
        return new Content().addMediaType("application/json",
                new MediaType().schema(new Schema<>().$ref("#/components/schemas/APIError")).example(errorsExample));
    }
}
