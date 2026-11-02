package com.refresh.javatask.responsedto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record APIError(
        @Schema(description = "Timestamp of the error", example = "2026-10-31T04:26:52.281Z")
        Instant timestamp,

        @Schema(description = "HTTP Status Code", example = "400")
        int status,

        @Schema(description = "HTTP Reason Phrase", example = "Bad Request")
        String error,

        @Schema(description = "Description of the error", example = "Validation Error")
        String message,

        @Schema(description = "Request path that caused the error", example = "/api/tasks")
        String path,

        @Schema(description = "Per-field validation errors, only present on validation failures")
        List<FieldViolation> fieldViolations
) {
}
