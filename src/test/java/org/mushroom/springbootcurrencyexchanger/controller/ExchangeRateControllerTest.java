package org.mushroom.springbootcurrencyexchanger.controller;

import org.junit.jupiter.api.Test;
import org.mushroom.springbootcurrencyexchanger.ExchangeRateController;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRateDto;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
import org.mushroom.springbootcurrencyexchanger.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeRateController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ExchangeRateService exchangeRateService;

    @Test
    void shouldReturnOk_whenGetAllExchangeRates() throws Exception {
        when(exchangeRateService.getAllExchangeRates()).thenReturn(List.of(new ExchangeRateDto()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exchangeRates"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOK_whenCreateNewExchangeRates() throws Exception {
        ExchangeRateDto responseDto = new ExchangeRateDto();
        CurrencyDto baseCurrency = new CurrencyDto();
        baseCurrency.setCode("someBaseCurrencyCode");
        CurrencyDto targetCurrency = new CurrencyDto();
        targetCurrency.setCode("someTargetCurrencyCode");

        responseDto.setBaseCurrency(baseCurrency);
        responseDto.setTargetCurrency(targetCurrency);
        responseDto.setRate(1.23);
        when(exchangeRateService.create(any(ExchangeRatesDto.class))).thenReturn(responseDto);

        // given
        String dto = """
                {
                    "baseCurrencyCode": "someBaseCurrencyCode",
                    "targetCurrencyCode": "someTargetCurrencyCode",
                    "rate": 1.23
                }
                """;

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/exchangeRates")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseCurrency.code").value("someBaseCurrencyCode"))
                .andExpect(jsonPath("$.targetCurrency.code").value("someTargetCurrencyCode"))
                .andExpect(jsonPath("$.rate").value(1.23));
    }

    @Test
    void shouldReturnOK_whenGetExchangeRateByCurrencyPair() throws Exception {
        ExchangeRateDto responseDto = new ExchangeRateDto();
        CurrencyDto baseCurrency = new CurrencyDto();
        baseCurrency.setCode("USD");
        CurrencyDto targetCurrency = new CurrencyDto();
        targetCurrency.setCode("EUR");

        responseDto.setBaseCurrency(baseCurrency);
        responseDto.setTargetCurrency(targetCurrency);
        responseDto.setRate(1.23);

        when(exchangeRateService.getByCurrencyPair("USD", "EUR")).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exchangeRates/USD/EUR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseCurrency.code").value("USD"))
                .andExpect(jsonPath("$.targetCurrency.code").value("EUR"))
                .andExpect(jsonPath("$.rate").value(1.23));
    }

    @Test
    void shouldReturnOK_whenUpdateExchangeRate() throws Exception {
        ExchangeRateDto responseDto = new ExchangeRateDto();
        CurrencyDto baseCurrency = new CurrencyDto();
        baseCurrency.setCode("USD");
        CurrencyDto targetCurrency = new CurrencyDto();
        targetCurrency.setCode("EUR");

        responseDto.setBaseCurrency(baseCurrency);
        responseDto.setTargetCurrency(targetCurrency);
        responseDto.setRate(1.25);

        when(exchangeRateService.update(any(ExchangeRatesDto.class))).thenReturn(responseDto);

        // given
        String dto = """
                {
                    "baseCurrencyCode": "USD",
                    "targetCurrencyCode": "EUR",
                    "rate": 1.25
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/exchangeRates")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseCurrency.code").value("USD"))
                .andExpect(jsonPath("$.targetCurrency.code").value("EUR"))
                .andExpect(jsonPath("$.rate").value(1.25));
    }

    @Test
    void shouldReturnNoContent_whenDeleteExchangeRateById() throws Exception {
        doNothing().when(exchangeRateService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exchangeRates/1"))
                .andExpect(status().isNoContent());
    }
}
