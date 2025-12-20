package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.cbrApi.CbrApiClient;
import org.example.dto.CbrApiGetResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CbrApiClient cbrApiClient;

    public List<CbrApiGetResponse> list(){

    }
}
