package org.mushroom.springbootcurrencyexchanger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRatesDto {
    private Long id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private double rate;
}
