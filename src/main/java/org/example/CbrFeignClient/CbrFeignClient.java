package org.example.CbrFeignClient;

import org.example.dto.CbrDailyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "currency-feign-client", url = "${cbr.api.base-url}" + "${cbr.api.daily-endpoint}")
public interface CbrFeignClient {

    @GetMapping
    CbrDailyResponse getDailyRates();
}
