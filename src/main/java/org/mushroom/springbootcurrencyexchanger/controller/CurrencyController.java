package org.mushroom.springbootcurrencyexchanger.controller;

import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.dto.NewCurrencyPayload;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyForChange;
import org.mushroom.springbootcurrencyexchanger.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public List<CurrencyDto> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/{code}")
    public CurrencyDto getCurrencyByCode(@PathVariable String code) {
        return currencyService.getByCodeCurrency(code);
    }

    @DeleteMapping("/{id}")
    public void deleteCurrency(@PathVariable Long id) {
        currencyService.deleteCurrencyById(id);
    }

    @PostMapping("api/currency/{code}")
    public CurrencyDto createCurrency(
            @RequestBody NewCurrencyPayload payload) {
        return currencyService.createCurrency(payload);
    }

    @PutMapping("/{id}")
    public CurrencyDto updateCurrency(
            @PathVariable Long id, @RequestBody CurrencyForChange change) {
        return currencyService.updateCurrency(id, change);
    }

    @PatchMapping("/{id}")
    public CurrencyDto patchCurrency(@PathVariable Long id, @RequestBody CurrencyForChange change) {
        return currencyService.updateCurrency(id, change);
    }
}
