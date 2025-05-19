package org.mushroom.springbootcurrencyexchanger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyForChange {
    private String code;
    private String fullName;
    private String sign;
}
