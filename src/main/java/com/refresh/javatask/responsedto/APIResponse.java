package com.refresh.javatask.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse<T> {
    @Schema(description = "Human-readable result message")
    private String message;

    @Schema(description = "Data returned by the API")
    private T data;

    public static <T> APIResponse<T> of(T data, String message) {
        APIResponse<T> response = new APIResponse<>();
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
