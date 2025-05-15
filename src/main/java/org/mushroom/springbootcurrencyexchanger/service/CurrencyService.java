package org.mushroom.springbootcurrencyexchanger.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.dto.NewCurrencyPayload;
import org.mushroom.springbootcurrencyexchanger.entity.Currency;
import org.mushroom.springbootcurrencyexchanger.repository.CurrencyRepository;
import org.mushroom.springbootcurrencyexchanger.util.CurrencyValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyValidator validator;

    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll()
                .stream()
                .map(CurrencyDto::fromCurrency).toList();
    }

    public CurrencyDto getByCodeCurrency(String code) {
        validator.validateCode(code);
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Валюта с кодом " + code + " не найдена"));
        return CurrencyDto.fromCurrency(currency);
    }

    public void deleteCurrencyById(Long id) {
        validator.validateId(id);
        currencyRepository.deleteById(id);
    }

    public CurrencyDto updateCurrency(Long id, NewCurrencyPayload payload) {
        validator.validate(payload);
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Валюта с id " + id + " не найдена"));
        currency.setCode(payload.getCode());
        currency.setFullName(payload.getFullName());
        currency.setSign(payload.getSign());
        Currency updateCurrency = currencyRepository.save(currency);
        return CurrencyDto.fromCurrency(updateCurrency);
    }

    public CurrencyDto createCurrency(NewCurrencyPayload payload) {
        Currency currency = new Currency(payload.getCode(), payload.getFullName(), payload.getSign());
        Currency savedCurrency = currencyRepository.save(currency);
        return CurrencyDto.fromCurrency(savedCurrency);
    }

}
