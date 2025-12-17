package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CbrApiRequest {
    private Integer amountInRubles;
    private String targetCurrency;
}
