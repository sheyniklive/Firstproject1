package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Validated
public class CbrApiConvertRequest {

    @NotBlank(message = "Сумма не может быть пустой")//определиться
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
    private BigDecimal amountInRubles;

    @NotBlank(message = "Код валюты не может быть пустым")//тоже
    @Size(min = 3, max = 3, message = "Код валюты должен состоять из 3-х букв")
    private String targetCurrency;
}
