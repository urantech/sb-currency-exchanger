package org.mushroom.springbootcurrencyexchanger.service;

import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.entity.Currency;
import org.mushroom.springbootcurrencyexchanger.repository.CurrencyRepository;
import org.mushroom.springbootcurrencyexchanger.util.CurrencyValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(CurrencyDto::fromCurrency)
                .toList();
    }

    public CurrencyDto getCurrencyByCode(String code) {
        CurrencyValidator.validateCode(code);
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Валюта с кодом " + code + " не найдена"));
        return CurrencyDto.fromCurrency(currency);
    }

    public void deleteById(Long id) {
        currencyRepository.deleteById(id);
    }

    public CurrencyDto update(CurrencyDto dto) {
        CurrencyValidator.validate(dto);
        Currency currency = currencyRepository.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Валюта с id " + dto.getId() + " не найдена"));
        currency.setCode(dto.getCode());
        currency.setFullName(dto.getFullName());
        currency.setSign(dto.getSign());
        Currency saved = currencyRepository.save(currency);
        return CurrencyDto.fromCurrency(saved);
    }

    public CurrencyDto create(CurrencyDto dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id must be null");
        }
        Currency currency = new Currency(dto.getCode(), dto.getFullName(), dto.getSign());
        Currency saved = currencyRepository.save(currency);
        return CurrencyDto.fromCurrency(saved);
    }
}
