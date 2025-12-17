package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class CbrApiGetResponse {
    private String currencyCode;
    private String currencyName;
    private Integer nominal;
    private BigDecimal value;
    private BigDecimal previous;
    private LocalDate date;
}
