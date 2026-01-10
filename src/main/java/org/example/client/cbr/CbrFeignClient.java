package org.example.client.cbr;

import org.example.dto.CbrDailyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "cbr-client",
        url = "${cbr.api.base-url}",
        configuration = FeignConfig.class)
public interface CbrFeignClient {

    @GetMapping("${cbr.api.daily-endpoint}")
    CbrDailyResponse getDailyRates();
}
