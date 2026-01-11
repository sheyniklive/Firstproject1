package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.cbr.Proxy;
import org.example.mappers.CbrApiMapper;
import org.example.dto.CbrApiConvertRequest;
import org.example.dto.CbrApiConvertResponse;
import org.example.dto.CbrApiGetAllResponse;
import org.example.dto.CbrApiGetByCodeResponse;
import org.example.dto.CbrDailyResponse;
import org.example.exception.CurrencyNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final Proxy proxy;

    public CbrApiGetAllResponse list() {
        CbrDailyResponse cbrResponse = proxy.getDailyRates();
        return CbrApiMapper.toGetAllResponse(cbrResponse);
    }

    public CbrApiGetByCodeResponse getByCode(String currencyCode) {
        CbrDailyResponse cbrResponse = proxy.getDailyRates();
        LocalDate date = cbrResponse.getDate().toLocalDate();
        CbrDailyResponse.Valute valute = getValuteByCode(currencyCode, cbrResponse);
        return CbrApiMapper.toGetByCodeResponse(valute, date);
    }

    public CbrApiConvertResponse convert(CbrApiConvertRequest request) {
        CbrDailyResponse cbrResponse = proxy.getDailyRates();

        if (request.getAmountInRubles().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма в рублях <= 0 (" + request.getAmountInRubles() + ")");
        }
        if (request.getTargetCurrency() == null || request.getTargetCurrency().isEmpty()) {
            throw new IllegalArgumentException("Передан неверный код валюты - " + request.getTargetCurrency());
        }
        CbrDailyResponse.Valute valute = getValuteByCode(request.getTargetCurrency(), cbrResponse);

        BigDecimal amountRubles = request.getAmountInRubles();
        BigDecimal rate = valute.getValue();
        Integer nominal = valute.getNominal();

        BigDecimal convertedAmount = converting(amountRubles, rate, nominal);

        return new CbrApiConvertResponse(
                "RUB",
                request.getTargetCurrency(),
                amountRubles,
                convertedAmount,
                rate,
                cbrResponse.getDate().toLocalDate());


    }

    private CbrDailyResponse.Valute getValuteByCode(String currencyCode, CbrDailyResponse cbrResponse) {
        CbrDailyResponse.Valute valute = cbrResponse.getValutes().get(currencyCode);
        if (valute == null) {
            throw new CurrencyNotFoundException(currencyCode);
        }
        return valute;
    }

    private BigDecimal converting(BigDecimal amountRubles, BigDecimal rate, Integer nominal) {
        return amountRubles.multiply(BigDecimal.valueOf(nominal))
                .divide(rate, 2, RoundingMode.HALF_UP);
    }
}
