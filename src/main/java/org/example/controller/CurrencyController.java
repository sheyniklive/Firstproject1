package org.example.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.dto.CbrApiConvertRequest;
import org.example.dto.CbrApiConvertResponse;
import org.example.dto.CbrApiGetAllResponse;
import org.example.dto.CbrApiGetByCodeResponse;
import org.example.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
@Validated
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<CbrApiGetAllResponse> getAll() {
        return ResponseEntity.ok(currencyService.list());
    }

    @GetMapping("/{currencyCode}")
    public ResponseEntity<CbrApiGetByCodeResponse> getByCode(@NotBlank(message = "Код валюты не может быть пустым")
                                                             @Size(min = 3, max = 3, message = "Код валюты должен состоять из 3-х букв")
                                                             @PathVariable String currencyCode) {
        return ResponseEntity.ok(currencyService.getByCode(currencyCode));
    }

    @PostMapping("/convert")
    public ResponseEntity<CbrApiConvertResponse> convert(@Valid @RequestBody CbrApiConvertRequest request) {
        return ResponseEntity.ok(currencyService.convert(request));
    }
}
