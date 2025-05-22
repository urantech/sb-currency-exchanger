package org.mushroom.springbootcurrencyexchanger.controller;

import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRateDto;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
import org.mushroom.springbootcurrencyexchanger.service.ExchangeRateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchangeRates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public List<ExchangeRateDto> getMany() {
        return exchangeRateService.getAllExchangeRates();
    }

    @GetMapping("/{baseCode}/{targetCode}")
    public ExchangeRateDto getOne(@PathVariable String baseCode, @PathVariable String targetCode) {
        return exchangeRateService.getByCurrencyPair(baseCode, targetCode);
    }

    @PostMapping
    public ExchangeRateDto create(@RequestBody ExchangeRatesDto dto) {
        return exchangeRateService.create(dto);
    }

    @PutMapping
    public ExchangeRateDto update(@RequestBody ExchangeRatesDto dto) {
        return exchangeRateService.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        exchangeRateService.deleteById(id);
    }
}
