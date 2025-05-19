package org.mushroom.springbootcurrencyexchanger.util;

import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ExchangeRateValidator {

    public void validateCurrencyPair(String baseCode, String targetCode) {
        validateCode(baseCode, "Код базовой валюты");
        validateCode(targetCode, "Код целевой валюты");
        if (baseCode.equals(targetCode)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Коды базовой и целевой валюты не могут совпадать"
            );
        }
    }

    public void validateCodes(ExchangeRatesDto payload) {
        validateCode(payload.getBaseCurrencyCode(), "Код базовой валюты");
        validateCode(payload.getTargetCurrencyCode(), "Код целевой валюты");
        if (payload.getBaseCurrencyCode().equals(payload.getTargetCurrencyCode())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Коды базовой и целевой валюты не могут совпадать"
            );
        }
        if (payload.getRate() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Курс обмена должен быть положительным"
            );
        }
    }

    private void validateCode(String code, String fieldName) {
        if (code == null || code.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " не может быть пустым"
            );
        }
        if (!code.matches("[A-Z]{3}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " должен состоять из 3 заглавных букв (например, USD)"
            );
        }
    }
}
