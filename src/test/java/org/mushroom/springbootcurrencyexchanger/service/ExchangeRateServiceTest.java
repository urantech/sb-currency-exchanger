package org.mushroom.springbootcurrencyexchanger.service;


import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRateDto;
import org.mushroom.springbootcurrencyexchanger.dto.ExchangeRatesDto;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ExchangeRateServiceTest {
    @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("truncate table exchange_rates restart identity cascade");
        jdbcTemplate.execute("truncate table currencies restart identity cascade");
        insertCurrency(12345L, "USD", "US Dollar", "$");
        insertCurrency(12346L, "EUR", "EURO", "€");
    }

    @Test
    void shouldReturnAllExchangeRates() {
        insertExchangeRate(1L, 12345L, 12346L, 0.85);

        List<ExchangeRateDto> actual = exchangeRateService.getAllExchangeRates();

        MatcherAssert.assertThat(actual, hasItem(allOf(
                hasProperty("baseCurrency", hasProperty("code", is("USD"))),
                hasProperty("targetCurrency", hasProperty("code", is("EUR"))),
                hasProperty("rate", closeTo(0.85, 0.0001)))));
    }

    @Test
    void shouldReturnExchangeRate_whenCurrencyPairExists() {
        insertExchangeRate(1L, 12345L, 12346L, 0.85);

        ExchangeRateDto actual = exchangeRateService.getByCurrencyPair("USD", "EUR");

        assertEquals("USD", actual.getBaseCurrency().getCode());
        assertEquals("EUR", actual.getTargetCurrency().getCode());
        assertEquals(0.85, actual.getRate(), 0.0001);
    }

    @Test
    void shouldThrowResponseStatusException_whenCurrencyPairDoesNotExist() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> exchangeRateService.getByCurrencyPair("EUR", "USD"));

        assertResponseStatusException(exception, HttpStatus.NOT_FOUND, "Курс для пары EUR/USD не найден");
    }

    @Test
    void shouldThrowResponseStatusException_whenCurrencyCodeIsInvalid() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> exchangeRateService.getByCurrencyPair("US", "EUR"));

        assertResponseStatusException(exception, HttpStatus.BAD_REQUEST, "Код валюты должен состоять из 3 символов");
    }

    @Test
    void shouldCreateExchangeRate_whenPayloadIsValid() {
        ExchangeRatesDto payload = new ExchangeRatesDto();
        payload.setBaseCurrencyCode("USD");
        payload.setTargetCurrencyCode("EUR");
        payload.setRate(0.85);

        ExchangeRateDto created = exchangeRateService.create(payload);

        assertEquals("USD", created.getBaseCurrency().getCode());
        assertEquals("EUR", created.getTargetCurrency().getCode());
        assertEquals(0.85, created.getRate(), 0.0001);
    }

    @Test
    void shouldThrowResponseStatusException_whenBaseCurrencyDoesNotExist() {
        ExchangeRatesDto payload = new ExchangeRatesDto();
        payload.setBaseCurrencyCode("XXX");
        payload.setTargetCurrencyCode("EUR");
        payload.setRate(0.85);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> exchangeRateService.create(payload));

        assertResponseStatusException(exception, HttpStatus.NOT_FOUND, "Базовая валюта XXX не найдена");
    }

    @Test
    void shouldThrowResponseStatusException_whenTargetCurrencyDoesNotExist() {
        ExchangeRatesDto payload = new ExchangeRatesDto();
        payload.setBaseCurrencyCode("USD");
        payload.setTargetCurrencyCode("XXX");
        payload.setRate(0.85);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> exchangeRateService.create(payload));

        assertResponseStatusException(exception, HttpStatus.NOT_FOUND, "Целевая валюта XXX не найдена");
    }

    @Test
    void shouldThrowResponseStatusException_whenPayloadCodesAreInvalid() {
        ExchangeRatesDto payload = new ExchangeRatesDto();
        payload.setBaseCurrencyCode("US");
        payload.setTargetCurrencyCode("EUR");
        payload.setRate(0.85);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> exchangeRateService.create(payload));

        assertResponseStatusException(exception, HttpStatus.BAD_REQUEST, "Код валюты должен состоять из 3 символов");
    }

    @Test
    void shouldUpdateExchangeRate_whenDtoIsValid() {
        insertExchangeRate(1L, 12345L, 12346L, 0.85);
        ExchangeRatesDto updateDto = new ExchangeRatesDto();
        updateDto.setId(1L);
        updateDto.setBaseCurrencyCode("EUR");
        updateDto.setTargetCurrencyCode("USD");
        updateDto.setRate(1.18);

        ExchangeRateDto updated = exchangeRateService.update(updateDto);

        assertEquals("EUR", updated.getBaseCurrency().getCode());
        assertEquals("USD", updated.getTargetCurrency().getCode());
        assertEquals(1.18, updated.getRate(), 0.0001);
    }


    @Test
    void shouldThrowResponseStatusException_whenExchangeRateIdDoesNotExist() {
        ExchangeRatesDto dto = new ExchangeRatesDto();
        dto.setId(999L);
        dto.setBaseCurrencyCode("USD");
        dto.setTargetCurrencyCode("EUR");
        dto.setRate(0.85);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> exchangeRateService.update(dto));

        assertResponseStatusException(exception, HttpStatus.NOT_FOUND, "Курс для пары не найден");
    }

    @Test
    void shouldThrowResponseStatusException_whenBaseCurrencyDoesNotExistInUpdate() {
        insertExchangeRate(1L, 12345L, 12346L, 0.85);
        ExchangeRatesDto dto = new ExchangeRatesDto();
        dto.setId(1L);
        dto.setBaseCurrencyCode("XXX");
        dto.setTargetCurrencyCode("EUR");
        dto.setRate(0.85);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> exchangeRateService.update(dto));

        assertResponseStatusException(exception, HttpStatus.NOT_FOUND, "Валюта не найдена");
    }

    @Test
    void shouldDeleteExchangeRate_byIdExists() {
        insertExchangeRate(1L, 12345L, 12346L, 0.85);
        insertExchangeRate(2L, 12346L, 12345L, 1.18);

        exchangeRateService.deleteById(1L);

        List<ExchangeRateDto> rates = exchangeRateService.getAllExchangeRates();
        assertEquals(1, rates.size());
        boolean usdEurExists = rates.stream()
                .anyMatch(dto -> "USD".equals(dto.getBaseCurrency().getCode()) && "EUR".equals(dto.getTargetCurrency().getCode()));
        assertFalse(usdEurExists);
    }

    @Test
    void shouldDoNothing_whenExchangeRateIdDoesNotExist() {
        insertExchangeRate(1L, 12345L, 12346L, 0.85);

        exchangeRateService.deleteById(999L);

        List<ExchangeRateDto> rates = exchangeRateService.getAllExchangeRates();
        assertEquals(1, rates.size());
    }

    private void insertCurrency(Long id, String code, String fullName, String sign) {
        String sql = "INSERT INTO currencies (id, code, full_name, sign) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, code, fullName, sign);
    }

    private void insertExchangeRate(Long id, Long baseCurrencyId, Long targetCurrencyId, double rate) {
        String sql = "INSERT INTO exchange_rates (id, base_currency_id, target_currency_id, rate) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, baseCurrencyId, targetCurrencyId, rate);
    }

    private void assertResponseStatusException(ResponseStatusException exception, HttpStatus status, String reason) {
        assertEquals(status, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }
}
