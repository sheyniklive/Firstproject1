package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.cbrApi.CbrApiClient;
import org.example.dto.CbrDailyResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CbrApiClient cbrApiClient;

    public CbrDailyResponse getRates(){
return cbrApiClient.getDailyRates();
    }
}
