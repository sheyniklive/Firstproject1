package org.example.cbrApi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cbr.api")
@Data
public class CbrApiProperties {
    private final String baseUrl;
    private final String dailyEndpoint;
    private final Integer timeout;
}
