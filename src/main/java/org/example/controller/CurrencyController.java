package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CbrApiGetAllResponse;
import org.example.dto.CbrApiGetByCodeResponse;
import org.example.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<CbrApiGetAllResponse> getAll(){
        return ResponseEntity.ok(currencyService.list());
    }

    @GetMapping("/{currencyCode}")
    public ResponseEntity<CbrApiGetByCodeResponse> getByCode(@PathVariable String currencyCode){
        return ResponseEntity.ok(currencyService.getByCode(currencyCode));
    }
}
