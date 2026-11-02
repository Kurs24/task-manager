package com.refresh.javatask.config;

import com.refresh.javatask.responsedto.APIError;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiErrorResponseConfig  {

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        ResolvedSchema errSchema =
                ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(APIError.class));
        Content content = new Content().addMediaType("application/json", new MediaType().schema(errSchema.schema));

        return openApi -> {
            errSchema.referencedSchemas.forEach(openApi.getComponents()::addSchemas);
            openApi.getPaths()
                    .forEach((path, item) -> item.readOperations().forEach(operation -> {
                                ApiResponses response = operation.getResponses();
                                addErrorIfNotExists(response, "500", "Internal Server Error", content);
                                addErrorIfNotExists(response, "400", "Bad Request", content);
                                if (path.contains("{")) {
                                    addErrorIfNotExists(response, "404", "Not Found", content);
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
}
