package org.mushroom.springbootcurrencyexchanger.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCurrencyPayload {
    private String code;
    private String fullName;
    private String sign;
}
