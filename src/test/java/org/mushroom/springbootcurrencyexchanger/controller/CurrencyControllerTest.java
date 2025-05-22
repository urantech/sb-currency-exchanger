package org.mushroom.springbootcurrencyexchanger.controller;

import org.junit.jupiter.api.Test;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CurrencyService currencyService;

    @Test
    void shouldReturnOk_whenGetAllCurrencies() throws Exception {
        when(currencyService.getAllCurrencies()).thenReturn(List.of(new CurrencyDto()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/currencies"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenCreateCurrency() throws Exception {
        when(currencyService.create(any(CurrencyDto.class))).thenReturn(new CurrencyDto());

        // given
        String dto = """
                {
                    "code": "someCode",
                    "fullName": "someName",
                    "sign": "someSign"
                }
                """;

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/currencies")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    void givenInvalidDto_whenCreateCurrency_thenReturnBadRequest() throws Exception {
        when(currencyService.create(any(CurrencyDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "some msg"));

        // given
        String dto = """
                {
                    "id": "1"
                    "code": "someCode",
                    "fullName": "someName",
                    "sign": "someSign"
                }
                """;

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/currencies")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }
}
