package org.mushroom.springbootcurrencyexchanger.service;

import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRateDto;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
import org.mushroom.springbootcurrencyexchanger.entity.Currency;
import org.mushroom.springbootcurrencyexchanger.entity.ExchangeRate;
import org.mushroom.springbootcurrencyexchanger.repository.CurrencyRepository;
import org.mushroom.springbootcurrencyexchanger.repository.ExchangeRateRepository;
import org.mushroom.springbootcurrencyexchanger.util.ExchangeRateValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public List<ExchangeRateDto> getAllExchangeRates() {
        return exchangeRateRepository.findAllFetchFields().stream()
                .map(ExchangeRateDto::fromEntity)
                .toList();
    }

    public ExchangeRateDto getByCurrencyPair(String baseCode, String targetCode) {
        ExchangeRateValidator.validateCurrencyPair(baseCode, targetCode);
        ExchangeRate rate = exchangeRateRepository.findByCurrencyPair(baseCode.toUpperCase(), targetCode.toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Курс для пары " + baseCode + "/" + targetCode + " не найден"));
        return ExchangeRateDto.fromEntity(rate);
    }

    public ExchangeRateDto create(ExchangeRatesDto payload) {
        ExchangeRateValidator.validateCodes(payload);

        Currency baseCurrency = currencyRepository.findByCode(payload.getBaseCurrencyCode().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Базовая валюта " + payload.getBaseCurrencyCode() + " не найдена"));

        Currency targetCurrency = currencyRepository.findByCode(payload.getTargetCurrencyCode().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Целевая валюта " + payload.getTargetCurrencyCode() + " не найдена"));

        ExchangeRate newRate = new ExchangeRate(baseCurrency, targetCurrency, payload.getRate());
        ExchangeRate saved = exchangeRateRepository.save(newRate);
        return ExchangeRateDto.fromEntity(saved);
    }

    public ExchangeRateDto update(ExchangeRatesDto dto) {
        ExchangeRate existingRate = exchangeRateRepository.findByIdWithCurrencies(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Курс для пары не найден"));
        Currency baseCurrency = currencyRepository.findByCode(dto.getBaseCurrencyCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Валюта не найдена"));
        Currency targetCurrency = currencyRepository.findByCode(dto.getTargetCurrencyCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Валюта не найдена"));

        existingRate.setBaseCurrency(baseCurrency);
        existingRate.setTargetCurrency(targetCurrency);
        existingRate.setRate(dto.getRate());
        ExchangeRate updated = exchangeRateRepository.save(existingRate);
        return ExchangeRateDto.fromEntity(
                exchangeRateRepository.findByIdWithCurrencies(updated.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Курс для пары не найден"))
        );
    }

    public void deleteById(Long id) {
        exchangeRateRepository.deleteById(id);
    }
}
