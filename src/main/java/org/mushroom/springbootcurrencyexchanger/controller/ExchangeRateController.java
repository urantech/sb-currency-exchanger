package org.mushroom.springbootcurrencyexchanger.controller;

import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRateDto;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
import org.mushroom.springbootcurrencyexchanger.service.ExchangeRateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/rates/")
    public List<ExchangeRateDto> getAllExchangeRates() {
        return exchangeRateService.getAllExchangeRates();
    }

    @GetMapping("/{baseCode}/{targetCode}")
    public ExchangeRateDto getExchangeRate(
            @PathVariable String baseCode,
            @PathVariable String targetCode) {
        return exchangeRateService.getByCurrencyPair(baseCode, targetCode);
    }

    @PostMapping("/{baseCode}/{targetCode}")
    public ExchangeRateDto createExchangeRate(
            @RequestBody ExchangeRatesDto payload) {
        return exchangeRateService.createExchangeRate(payload);
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
