package org.mushroom.springbootcurrencyexchanger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mushroom.springbootcurrencyexchanger.entity.ExchangeRate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDto {
    private long id;
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private double rate;

    public static ExchangeRateDto fromEntity(ExchangeRate exchangeRate) {
        ExchangeRateDto result = new ExchangeRateDto();
        result.setId(exchangeRate.getId());
        result.setBaseCurrency(CurrencyDto.fromCurrency(exchangeRate.getBaseCurrency()));
        result.setTargetCurrency(CurrencyDto.fromCurrency(exchangeRate.getTargetCurrency()));
        result.setRate(exchangeRate.getRate());
        return result;
    }
}
