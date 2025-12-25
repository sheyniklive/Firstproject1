package org.example.cbrApi;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfig {

    @Bean
    public OkHttpClient createClient(CbrApiProperties cbrApiProperties){
        return new OkHttpClient.Builder()
                .connectTimeout(cbrApiProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(cbrApiProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(cbrApiProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
}
