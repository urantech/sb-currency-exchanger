package org.mushroom.springbootcurrencyexchanger.service;

import jakarta.persistence.EntityNotFoundException;
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

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateValidator exchangeRateValidator;

    public List<ExchangeRateDto> getAllExchangeRates() {
        return exchangeRateRepository.findAllExchangeRates();
    }

    public ExchangeRateDto getByCurrencyPair(String baseCode, String targetCode) {
        exchangeRateValidator.validateCurrencyPair(baseCode, targetCode);
        ExchangeRate rate = exchangeRateRepository
                .findByCurrencyPair(baseCode.toUpperCase(), targetCode.toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Курс для пары " + baseCode + "/" + targetCode + " не найден"));
        return ExchangeRateDto.fromEntities(rate, rate.getBaseCurrency(), rate.getTargetCurrency());
    }

    public ExchangeRateDto createExchangeRate(ExchangeRatesDto payload) {
        exchangeRateValidator.validateCodes(payload);

        Currency baseCurrency = currencyRepository.findByCode(payload.getBaseCurrencyCode().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Базовая валюта " + payload.getBaseCurrencyCode() + " не найдена"));

        Currency targetCurrency = currencyRepository.findByCode(payload.getTargetCurrencyCode().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Целевая валюта " + payload.getTargetCurrencyCode() + " не найдена"));

        ExchangeRate newRate = new ExchangeRate(baseCurrency, targetCurrency, payload.getRate());
        ExchangeRate saved = exchangeRateRepository.save(newRate);
        return ExchangeRateDto.fromEntities(saved, saved.getBaseCurrency(), saved.getTargetCurrency());
    }

    public ExchangeRateDto updateExchangeRate(String baseCode, String targetCode, ExchangeRatesDto payload) {
        exchangeRateValidator.validateCurrencyPair(baseCode, targetCode);

        ExchangeRate existingRate = exchangeRateRepository
                .findByCurrencyPair(baseCode.toUpperCase(), targetCode.toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Курс для пары " + baseCode + "/" + targetCode + " не найден"));

        existingRate.setRate(payload.getRate());
        ExchangeRate updated = exchangeRateRepository.save(existingRate);
        return ExchangeRateDto.fromEntities(updated, updated.getBaseCurrency(), updated.getTargetCurrency());
    }

    public void deleteExchangeRate(String baseCode, String targetCode) {
        exchangeRateValidator.validateCurrencyPair(baseCode, targetCode);
        ExchangeRate rate = exchangeRateRepository
                .findByCurrencyPair(baseCode.toUpperCase(), targetCode.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Курс для пары " + baseCode + "/" + targetCode + " не найден"));
        exchangeRateRepository.delete(rate);
    }
}
