package org.mushroom.springbootcurrencyexchanger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.entity.Currency;
import org.mushroom.springbootcurrencyexchanger.entity.ExchangeRate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDto {
    private long id;
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private double rate;

    public static ExchangeRateDto fromEntities(
            ExchangeRate exchangeRate, Currency baseCurrency, Currency targetCurrency) {
        CurrencyDto baseCurrencyDto = CurrencyDto.fromCurrency(baseCurrency);
        CurrencyDto targetCurrencyDto = CurrencyDto.fromCurrency(targetCurrency);

        ExchangeRateDto result = new ExchangeRateDto();
        result.setId(exchangeRate.getId());
        result.setBaseCurrency(baseCurrencyDto);
        result.setTargetCurrency(targetCurrencyDto);
        result.setRate(exchangeRate.getRate());
        return result;
    }
}

