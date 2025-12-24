package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.cbrApi.CbrApiClient;
import org.example.cbrApi.CbrApiMapper;
import org.example.dto.CbrApiGetAllResponse;
import org.example.dto.CbrApiGetByCodeResponse;
import org.example.dto.CbrDailyResponse;
import org.example.exception.CurrencyNotFoundException;
import org.springframework.stereotype.Service;

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
        CbrDailyResponse.Valute valute = cbrResponse.getValutes().get(currencyCode);
        if (valute == null) {
            throw new CurrencyNotFoundException(currencyCode);
        }
        return CbrApiMapper.toGetByCodeResponse(valute, date);
    }
}
