package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
public class CbrApiGetByCodeResponse {
    private String currencyCode;
    private String currencyName;
    private Integer nominal;
    private BigDecimal value;
    private BigDecimal previous;
    private LocalDate date;
}
