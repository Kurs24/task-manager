package com.refresh.javatask.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FieldViolation(
        @Schema(description = "Field name that caused the error", example = "title")
        String field,

        @Schema(description = "Error message", example = "Title is required")
        String message
) {
}
