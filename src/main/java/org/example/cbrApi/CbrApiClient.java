package org.example.cbrApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.example.dto.CbrDailyResponse;
import org.example.exception.CbrApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CbrApiClient {
    private static final Logger log = LoggerFactory.getLogger(CbrApiClient.class);
    private final OkHttpClient client;
    private final CbrApiProperties properties;
    private final ObjectMapper mapper;

    public CbrDailyResponse getDailyRates() {
        String url = properties.getBaseUrl() + properties.getDailyEndpoint();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CbrApiException("CbrApi response code " + response.code());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new CbrApiException("CbrApi response body is null");
            }
            return mapper.readValue(body.string(), CbrDailyResponse.class);
        } catch (IOException e) {
            log.error("Failed to get daily Rates", e);
            throw new CbrApiException("Failed to get daily Rates", e);
        }
    }
}
