package com.refresh.javatask.responsedto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse<T> {
    private String message;
    private T data;

    public static <T> APIResponse<T> of(T data, String message) {
        APIResponse<T> response = new APIResponse<>();
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
