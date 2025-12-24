package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CbrApiGetByCodeResponse {
    private String currencyCode;
    private String currencyName;
    private Integer nominal;
    private BigDecimal value;
    private BigDecimal previous;
    private LocalDate date;
}
