package org.mushroom.springbootcurrencyexchanger.util;

import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeRateValidator {
    public void validateCodes(ExchangeRatesDto payload) {
        List<String> errors = new ArrayList<>();

        if (payload.getBaseCurrencyCode() == null || payload.getBaseCurrencyCode().trim().isEmpty()) {
            errors.add("Код базовой валюты не может быть пустым или null");
        } else if (payload.getBaseCurrencyCode().length() != 3) {
            errors.add("Код базовой валюты должен состоять из 3 символов");
        }

        if (payload.getTargetCurrencyCode() == null || payload.getTargetCurrencyCode().trim().isEmpty()) {
            errors.add("Код целевой валюты не может быть пустым или null");
        } else if (payload.getTargetCurrencyCode().length() != 3) {
            errors.add("Код целевой валюты должен состоять из 3 символов");
        }

        if (payload.getBaseCurrencyCode() != null && payload.getTargetCurrencyCode() != null
            && payload.getBaseCurrencyCode().equalsIgnoreCase(payload.getTargetCurrencyCode())) {
            errors.add("Базовая и целевая валюты не могут быть одинаковыми");
        }

        if (payload.getRate() <= 0) {
            errors.add("Курс обмена должен быть положительным");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Ошибки валидации: " + String.join("; ", errors));
        }
    }

    public void validateCurrencyPair(String baseCode, String targetCode) {
        List<String> errors = new ArrayList<>();

        if (baseCode == null || baseCode.trim().isEmpty()) {
            errors.add("Код базовой валюты не может быть пустым или null");
        } else if (baseCode.length() != 3) {
            errors.add("Код базовой валюты должен состоять из 3 символов");
        }

        if (targetCode == null || targetCode.trim().isEmpty()) {
            errors.add("Код целевой валюты не может быть пустым или null");
        } else if (targetCode.length() != 3) {
            errors.add("Код целевой валюты должен состоять из 3 символов");
        }

        if (baseCode != null && targetCode != null && baseCode.equalsIgnoreCase(targetCode)) {
            errors.add("Базовая и целевая валюты не могут быть одинаковыми");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Ошибки валидации валютной пары: " + String.join("; ", errors));
        }
    }
}
