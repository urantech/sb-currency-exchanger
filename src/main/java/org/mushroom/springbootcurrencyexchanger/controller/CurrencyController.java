package org.mushroom.springbootcurrencyexchanger.controller;

import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public List<CurrencyDto> getMany() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/currency/{code}")
    public CurrencyDto getOne(@PathVariable String code) {
        return currencyService.getCurrencyByCode(code);
    }

    @PostMapping
    public CurrencyDto create(@RequestBody CurrencyDto dto) {
        return currencyService.create(dto);
    }

    @PutMapping
    public CurrencyDto update(@RequestBody CurrencyDto dto) {
        return currencyService.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        currencyService.deleteById(id);
    }
}
