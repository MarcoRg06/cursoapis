package com.cursoapis.cursoapis.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        String message,
        int statusCode,
        Map<String, String> errors,
        LocalDateTime timestamp
) {}
