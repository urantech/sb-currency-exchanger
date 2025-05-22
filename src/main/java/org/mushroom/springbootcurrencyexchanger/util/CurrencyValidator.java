package org.mushroom.springbootcurrencyexchanger.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyValidator {

    public static void validate(CurrencyDto dto) throws ResponseStatusException {
        if (dto == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Объект валюты не может быть null"
            );
        }
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты не может быть пустым или null"
            );
        }
        if (dto.getCode().length() != 3) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты должен состоять из 3 символов"
            );
        }
        if (!dto.getCode().matches("[A-Z]{3}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Код валюты должен состоять из 3 заглавных букв (например, USD)"
            );
        }
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Полное имя валюты не может быть пустым или null"
            );
        }
        if (dto.getSign() == null || dto.getSign().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Символ валюты не может быть пустым или null"
            );
        }
    }

    public static void validateCode(String code) {
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
}
