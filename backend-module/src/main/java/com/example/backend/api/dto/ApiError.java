package com.example.backend.api.dto;

import java.time.OffsetDateTime;
import lombok.Getter;

@Getter
public class ApiError {
    private int status;
    private String message;
    private String path;
    private OffsetDateTime timestamp = OffsetDateTime.now();

    public ApiError(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }

}