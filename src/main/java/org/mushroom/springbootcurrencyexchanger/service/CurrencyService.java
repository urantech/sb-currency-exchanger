package org.mushroom.springbootcurrencyexchanger.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.dto.NewCurrencyPayload;
import org.mushroom.springbootcurrencyexchanger.entity.Currency;
import org.mushroom.springbootcurrencyexchanger.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll().stream().map(CurrencyDto::fromCurrency).collect(Collectors.toList());
    }

    public CurrencyDto getByCodeCurrency(String code) {
        Currency currency = currencyRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("Валюта с кодом " + code + " не найдена"));
        return CurrencyDto.fromCurrency(currency);
    }

    public void deleteCurrencyById(Long id) {
        currencyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Валюта с id " + id + " не найдена"));
        currencyRepository.deleteById(id);
    }

    public CurrencyDto updateCurrency(String idParam, NewCurrencyPayload payload) {
        Long id = Long.parseLong(idParam);
        Currency currency = currencyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Валюта с id " + id + " не найдена"));
        currency.setCode(payload.getCode() != null ? payload.getCode() : currency.getCode());
        currency.setFullName((payload.getFullName() != null ? payload.getFullName() : currency.getFullName()));
        currency.setSign(payload.getSign() != null ? payload.getSign() : currency.getSign());
        Currency updateCurrency = currencyRepository.save(currency);
        return CurrencyDto.fromCurrency(updateCurrency);
    }

    public CurrencyDto createCurrency(NewCurrencyPayload payload) {
        Currency currency = new Currency(payload.getCode(), payload.getFullName(), payload.getSign());
        Currency savedCurrency = currencyRepository.save(currency);
        return CurrencyDto.fromCurrency(savedCurrency);
    }

}
