package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
public class CbrApiConvertResponse {
private String fromCurrency;
private String toCurrency;
private BigDecimal amountInRubles;
private BigDecimal convertedAmount;
private BigDecimal rate;
private LocalDate date;
}
