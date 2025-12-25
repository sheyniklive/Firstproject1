package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.cbrApi.CbrApiClient;
import org.example.cbrApi.CbrApiMapper;
import org.example.dto.*;
import org.example.exception.CurrencyNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CbrApiClient cbrApiClient;

    public CbrApiGetAllResponse list() {
        CbrDailyResponse cbrResponse = cbrApiClient.getDailyRates();
        return CbrApiMapper.toGetAllResponse(cbrResponse);
    }

    public CbrApiGetByCodeResponse getByCode(String currencyCode) {
        CbrDailyResponse cbrResponse = cbrApiClient.getDailyRates();
        LocalDate date = cbrResponse.getDate().toLocalDate();
        CbrDailyResponse.Valute valute = getValuteByCode(currencyCode, cbrResponse);
        return CbrApiMapper.toGetByCodeResponse(valute, date);
    }

    public CbrApiConvertResponse convert(CbrApiConvertRequest request) {
        CbrDailyResponse cbrResponse = cbrApiClient.getDailyRates();

        if (request.getAmountInRubles().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма в рублях <= 0 (" + request.getAmountInRubles() + ")");
        }
        if (request.getTargetCurrency().isEmpty()) {
            throw new IllegalArgumentException("Передан неверный код валюты - " + request.getTargetCurrency());
        }

        CbrDailyResponse.Valute valute = getValuteByCode(request.getTargetCurrency(), cbrResponse);

        CbrApiConvertResponse response = new CbrApiConvertResponse();
        response.setFromCurrency("RUB");
        response.setToCurrency(request.getTargetCurrency());
        response.setAmountInRubles(request.getAmountInRubles());
response.setConvertedAmount(converting());
        response.setRate(valute.getValue());
        response.setDate(cbrResponse.getDate().toLocalDate());


    }

    private CbrDailyResponse.Valute getValuteByCode(String currencyCode, CbrDailyResponse cbrResponse) {
        CbrDailyResponse.Valute valute = cbrResponse.getValutes().get(currencyCode);
        if (valute == null) {
            throw new CurrencyNotFoundException(currencyCode);
        }
        return valute;
    }

    private BigDecimal converting() {

    }
}
