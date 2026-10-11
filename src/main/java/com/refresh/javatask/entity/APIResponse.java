package com.refresh.javatask.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse<T> {
    private String message;
    private T data;
}
