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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateValidator exchangeRateValidator;

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAllExchangeRates();
    }

    public ExchangeRateDto getByCurrencyPair(String baseCode, String targetCode) {
        exchangeRateValidator.validateCurrencyPair(baseCode, targetCode);
        ExchangeRate rate = exchangeRateRepository
                .findByCurrencyPair(baseCode.toUpperCase(), targetCode.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Курс для пары " + baseCode + "/" + targetCode + " не найден"));
        return convertToDto(rate);
    }

    public ExchangeRateDto createExchangeRate(ExchangeRatesDto payload) {
        exchangeRateValidator.validateCodes(payload);

        Currency baseCurrency = currencyRepository.findByCode(payload.getBaseCurrencyCode().toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Базовая валюта " + payload.getBaseCurrencyCode() + " не найдена"));

        Currency targetCurrency = currencyRepository.findByCode(payload.getTargetCurrencyCode().toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Целевая валюта " + payload.getTargetCurrencyCode() + " не найдена"));

        if (exchangeRateRepository.existsByCurrencyPair(
                payload.getBaseCurrencyCode().toUpperCase(),
                payload.getTargetCurrencyCode().toUpperCase())) {
            throw new IllegalArgumentException(
                    "Курс для пары " + payload.getBaseCurrencyCode() + "/" + payload.getTargetCurrencyCode() + " уже существует");
        }

        ExchangeRate newRate = new ExchangeRate(baseCurrency, targetCurrency, payload.getRate());
        ExchangeRate saved = exchangeRateRepository.save(newRate);
        return convertToDto(saved);
    }

    public ExchangeRateDto updateExchangeRate(String baseCode, String targetCode, ExchangeRatesDto payload) {
        exchangeRateValidator.validateCurrencyPair(baseCode, targetCode);

        ExchangeRate existingRate = exchangeRateRepository
                .findByCurrencyPair(baseCode.toUpperCase(), targetCode.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Курс для пары " + baseCode + "/" + targetCode + " не найден"));

        existingRate.setRate(payload.getRate());
        ExchangeRate updated = exchangeRateRepository.save(existingRate);
        return convertToDto(updated);
    }

    public void deleteExchangeRate(String baseCode, String targetCode) {
        exchangeRateValidator.validateCurrencyPair(baseCode, targetCode);
        ExchangeRate rate = exchangeRateRepository
                .findByCurrencyPair(baseCode.toUpperCase(), targetCode.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Курс для пары " + baseCode + "/" + targetCode + " не найден"));
        exchangeRateRepository.delete(rate);
    }

    private ExchangeRateDto convertToDto(ExchangeRate exchangeRate) {
        return ExchangeRateDto.fromEntities(
                exchangeRate,
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency());
    }
}
