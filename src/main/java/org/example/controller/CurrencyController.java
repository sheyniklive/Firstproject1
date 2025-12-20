package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CbrApiGetResponse;
import org.example.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CbrApiGetResponse>>  getAllCurrencies(){
        return ResponseEntity.ok(currencyService.list());
    }

}
