package com.ingweb.springboot.web.app.ApiResponse;
import lombok.Data;

@Data
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
}
