package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CbrApiConvertRequest {
    private BigDecimal amountInRubles;
    private String targetCurrency;
}
