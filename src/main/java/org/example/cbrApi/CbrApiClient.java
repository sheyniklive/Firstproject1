package org.example.cbrApi;

import lombok.Data;
import org.example.dto.CbrApiGetResponse;
import org.springframework.stereotype.Component;

@Component
@Data
public class CbrApiClient {
    private final OkHttpClient client;
    private final CbrApiProperties properties;

    public CbrApiGetResponse getDailyRates() {


    }
}
