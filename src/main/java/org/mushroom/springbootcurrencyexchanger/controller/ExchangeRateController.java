package org.mushroom.springbootcurrencyexchanger.controller;

import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRateDto;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
import org.mushroom.springbootcurrencyexchanger.entity.ExchangeRate;
import org.mushroom.springbootcurrencyexchanger.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/exchangeRates/")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/rates/")
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateService.getAllExchangeRates();
    }

    @GetMapping("/{baseCode}/{targetCode}")
    public ExchangeRateDto getExchangeRate(
            @PathVariable String baseCode,
            @PathVariable String targetCode) {
        return exchangeRateService.getByCurrencyPair(baseCode, targetCode);
    }

    @PostMapping
    public ResponseEntity<ExchangeRateDto> createExchangeRate(
            @RequestBody ExchangeRatesDto payload,
            UriComponentsBuilder uriComponentsBuilder) {
        ExchangeRateDto created = exchangeRateService.createExchangeRate(payload);
        return ResponseEntity.created(uriComponentsBuilder
                        .path("/api/exchangeRates/{baseCode}/{targetCode}")
                        .buildAndExpand(created.getBaseCurrency().getCode(), created.getTargetCurrency().getCode())
                        .toUri())
                .body(created);
    }

    @PutMapping("/{baseCode}/{targetCode}")
    public ExchangeRateDto updateExchangeRate(
            @PathVariable String baseCode,
            @PathVariable String targetCode,
            @RequestBody ExchangeRatesDto payload) {
        return exchangeRateService.updateExchangeRate(baseCode, targetCode, payload);
    }

    @DeleteMapping("/{baseCode}/{targetCode}")
       public void deleteExchangeRate(
            @PathVariable String baseCode,
            @PathVariable String targetCode) {
        exchangeRateService.deleteExchangeRate(baseCode, targetCode);
    }
}
