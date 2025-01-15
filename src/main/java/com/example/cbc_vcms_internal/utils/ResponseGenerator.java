package com.example.cbc_vcms_internal.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseGenerator {

    // Success response with info
    public static ResponseEntity<Object> success(String message, Object info) {
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", message,
            "info", info
        ));
    }

    // Success response without info
    public static ResponseEntity<Object> success(String message) {
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", message
        ));
    }

    // Error response
    public static ResponseEntity<Object> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
            "status", "failed",
            "message", message
        ));
    }
}
