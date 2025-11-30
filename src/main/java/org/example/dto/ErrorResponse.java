package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
}
