package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CbrApiConvertRequest {

    @NotNull(message = "Сумма не может быть пустой")
    @DecimalMin(value = "0", inclusive = false, message = "Сумма должна быть больше 0")
    private BigDecimal amountInRubles;

    @NotBlank(message = "Код валюты не может быть пустым")
    @Size(min = 3, max = 3, message = "Код валюты должен состоять из 3-х букв верхнего регистра")
    private String targetCurrency;
}
