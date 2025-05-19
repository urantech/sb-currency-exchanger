package org.mushroom.springbootcurrencyexchanger.util;

import org.mushroom.springbootcurrencyexchanger.dto.CurrencyForChange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CurrencyValidator {

    public void validate(CurrencyForChange change) throws ResponseStatusException {
        if (change == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Объект валюты не может быть null"
            );
        }
        if (change.getCode() == null || change.getCode().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты не может быть пустым или null"
            );
        }
        if (change.getCode().length() != 3) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты должен состоять из 3 символов"
            );
        }
        if (!change.getCode().matches("[A-Z]{3}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты должен состоять из 3 заглавных букв (например, USD)"
            );
        }
        if (change.getFullName() == null || change.getFullName().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Полное имя валюты не может быть пустым или null"
            );
        }
        if (change.getSign() == null || change.getSign().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Символ валюты не может быть пустым или null"
            );
        }
    }

    public void validateCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты не может быть пустым или null"
            );
        }
        if (code.length() != 3) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты должен состоять из 3 символов"
            );
        }
        if (!code.matches("[A-Z]{3}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты должен состоять из 3 заглавных букв (например, USD)"
            );
        }
    }

    public void validateId(Long id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ID валюты не может быть null"
            );
        }
        if (id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ID валюты должен быть положительным"
            );
        }
    }
}
