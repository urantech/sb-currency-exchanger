package org.mushroom.springbootcurrencyexchanger.service;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mushroom.springbootcurrencyexchanger.dto.CurrencyDto;
import org.mushroom.springbootcurrencyexchanger.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CurrencyServiceTest {
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("truncate table currencies restart identity cascade");
        insertCurrency(12345L, "USD", "US Dollar", "$");
        insertCurrency(12346L, "EUR", "EURO", "€");
    }
    @Test
    void shouldReturnAllCurrencies() {
        List<CurrencyDto> actual = currencyService.getAllCurrencies();

        MatcherAssert.assertThat(actual, containsInAnyOrder(
                allOf(
                        hasProperty("code", is("USD")),
                        hasProperty("fullName", is("US Dollar")),
                        hasProperty("sign", is("$"))),
                allOf(
                        hasProperty("code", is("EUR")),
                        hasProperty("fullName", is("EURO")),
                        hasProperty("sign", is("€")))));
    }

    @Test
    void shouldReturnCurrency_whenGetCurrencyByCode() {
        CurrencyDto actual = currencyService.getCurrencyByCode("USD");

        assertEquals("USD", actual.getCode());
        assertEquals("US Dollar", actual.getFullName());
        assertEquals("$", actual.getSign());
    }

    @Test
    void shouldThrowResponseStatusException_whenCodeDoesNotExist() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> currencyService.getCurrencyByCode("XXX"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Валюта с кодом XXX не найдена", exception.getReason());
    }

    @Test
    void shouldThrowResponseStatusException_whenCodeIsInvalid() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> currencyService.getCurrencyByCode("US"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Код валюты должен состоять из 3 символов", exception.getReason());
    }

    @Test
    void shouldDeleteCurrency_whenIdExists() {
        insertCurrency(12347L, "JPY", "Japan Yen", "¥");
        Long id = jdbcTemplate.queryForObject("SELECT id FROM currencies WHERE code = 'JPY'", Long.class);

        currencyService.deleteById(id);

        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        assertEquals(2, currencies.size());
        boolean jpyExists = currencies.stream().anyMatch(c -> "JPY".equals(c.getCode()));
        assertFalse(jpyExists);
    }

    @Test
    void shouldDoNothing_whenIdDoesNotExist() {
        currencyService.deleteById(999L);

        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        assertEquals(2, currencies.size());
        boolean jpyExists = currencies.stream().anyMatch(c -> "JPY".equals(c.getCode()));
        assertFalse(jpyExists);
    }

    @Test
    void shouldUpdateCurrency_whenDtoIsValid() {
        CurrencyDto dto = new CurrencyDto();
        dto.setId(12345L);
        dto.setCode("JPY");
        dto.setFullName("Japan Yen");
        dto.setSign("¥");

        CurrencyDto updated = currencyService.update(dto);

        assertEquals(dto.getCode(), updated.getCode());
        assertEquals("Japan Yen", updated.getFullName());
        assertEquals("¥", updated.getSign());
    }

    @Test
    void shouldThrowNotFound_whenDtoIsInvalid() {
        CurrencyDto dto = new CurrencyDto();
        dto.setId(999L);
        dto.setCode("JPY");
        dto.setFullName("Japan Yen");
        dto.setSign("¥");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> currencyService.update(dto));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Валюта с id 999 не найдена", exception.getReason());
    }

    private void insertCurrency(Long id, String code, String fullName, String sign) {
        jdbcTemplate.execute(String.format("INSERT INTO currencies (id, code, full_name, sign) VALUES (%d, '%s', '%s', '%s')",
                id, code, fullName, sign));
    }
}
