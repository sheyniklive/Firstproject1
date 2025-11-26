package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
}
