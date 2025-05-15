package org.mushroom.springbootcurrencyexchanger.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRatesDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private double rate;
}
