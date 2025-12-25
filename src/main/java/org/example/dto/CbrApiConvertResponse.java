package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class CbrApiConvertResponse {
private String fromCurrency;
private String toCurrency;
private BigDecimal amountInRubles;
private BigDecimal convertedAmount;
private BigDecimal rate;
private LocalDate date;
}
