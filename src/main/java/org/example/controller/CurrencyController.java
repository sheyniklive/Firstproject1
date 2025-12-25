package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CbrApiConvertRequest;
import org.example.dto.CbrApiConvertResponse;
import org.example.dto.CbrApiGetAllResponse;
import org.example.dto.CbrApiGetByCodeResponse;
import org.example.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/convert")
    public ResponseEntity<CbrApiConvertResponse> convert(@RequestBody CbrApiConvertRequest request){
        return ResponseEntity.ok(currencyService.convert(request));
    }
}
