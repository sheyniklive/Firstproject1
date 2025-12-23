package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CbrDailyResponse;
import org.example.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<CbrDailyResponse>  getAllCurrencies(){
        return ResponseEntity.ok(currencyService.getRates());
    }

}
