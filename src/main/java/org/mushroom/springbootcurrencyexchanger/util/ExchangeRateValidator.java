package org.mushroom.springbootcurrencyexchanger.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateValidator {

    public static void validateCurrencyPair(String baseCode, String targetCode) {
        CurrencyValidator.validateCode(baseCode);
        CurrencyValidator.validateCode(targetCode);
        if (baseCode.equals(targetCode)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Коды базовой и целевой валюты не могут совпадать"
            );
        }
    }

    public static void validateCodes(ExchangeRatesDto payload) {
        CurrencyValidator.validateCode(payload.getBaseCurrencyCode());
        CurrencyValidator.validateCode(payload.getTargetCurrencyCode());
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
}
