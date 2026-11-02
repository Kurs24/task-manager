package com.refresh.javatask.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A single field validation error")
public record FieldViolation(
        @Schema(description = "Field name that caused the error", example = "title")
        String field,

        @Schema(description = "Error message", example = "Title is required")
        String message
) {
}