package org.mushroom.springbootcurrencyexchanger.dto;

import lombok.Data;
import org.mushroom.springbootcurrencyexchanger.entity.Currency;

@Data
public class CurrencyDto {
    private Long id;
    private String code;
    private String fullName;
    private String sign;

    public static CurrencyDto fromCurrency(Currency currency) {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currency.getId());
        currencyDto.setCode(currency.getCode());
        currencyDto.setFullName(currency.getFullName());
        currencyDto.setSign(currency.getSign());
        return currencyDto;
    }
}
