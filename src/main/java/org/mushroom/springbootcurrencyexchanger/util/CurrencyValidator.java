package org.mushroom.springbootcurrencyexchanger.util;

import org.mushroom.springbootcurrencyexchanger.dto.NewCurrencyPayload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CurrencyValidator {
    public void validate(NewCurrencyPayload payload) {
        List<String> errors = new ArrayList<>();

        if (payload.getCode() == null || payload.getCode().trim().isEmpty()) {
            errors.add("Код валюты не может быть пустым или null");
        }
        if (payload.getFullName() == null || payload.getFullName().trim().isEmpty()) {
            errors.add("Полное имя валюты не может быть пустым или null");
        }
        if (payload.getSign() == null || payload.getSign().trim().isEmpty()) {
            errors.add("Символ валюты не может быть пустым или null");
        }
        if (payload.getCode() != null && payload.getCode().length() != 3) {
            errors.add("Код валюты должен состоять из 3 символов");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Ошибки валидации: " + String.join("; ", errors));
        }
    }

    public void validateCode(String code) {
        List<String> errors = new ArrayList<>();

        if (code == null || code.trim().isEmpty()) {
            errors.add("Код валюты не может быть пустым или null");
        }
        if (code != null && code.length() != 3) {
            errors.add("Код валюты должен состоять из 3 символов");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Ошибки валидации кода валюты: " + String.join("; ", errors));
        }
    }

    public void validateId(Long id) {
        List<String> errors = new ArrayList<>();

        if (id == null) {
            errors.add("ID валюты не может быть null");
        }
        if (id != null && id <= 0) {
            errors.add("ID валюты должен быть положительным");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Ошибки валидации ID валюты: " + String.join("; ", errors));
        }
    }
}
