package org.mushroom.springbootcurrencyexchanger.controller;

import jakarta.servlet.http.HttpServlet;
import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.dto.NewCurrencyPayload;
import org.mushroom.springbootcurrencyexchanger.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/currency")
@RequiredArgsConstructor
public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{code}")
    public ResponseEntity<CurrencyDto> getCurrencyByCode(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.getByCodeCurrency(code));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrency(@PathVariable Long id) {
        currencyService.deleteCurrencyById(id);
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(
            @RequestBody NewCurrencyPayload payload, UriComponentsBuilder uriComponentsBuilder) {
        CurrencyDto createCurrency = currencyService.createCurrency(payload);
        return ResponseEntity.created(uriComponentsBuilder.path("api/currency/{code}")
                        .build(createCurrency
                                .getCode()))
                .body(createCurrency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDto> updateCurrency(
            @PathVariable Long id, @RequestBody NewCurrencyPayload payload) {
        return ResponseEntity.ok(currencyService.updateCurrency(id.toString(), payload));
    }
}
