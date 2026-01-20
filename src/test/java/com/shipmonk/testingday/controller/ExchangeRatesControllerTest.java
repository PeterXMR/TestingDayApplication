package com.shipmonk.testingday.controller;

import com.shipmonk.testingday.api.ExchangeRate;
import com.shipmonk.testingday.exception.ExchangeRateNotFoundException;
import com.shipmonk.testingday.service.ExchangeRateService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for ExchangeRatesController.
 * Tests the REST endpoint behavior using Spring Boot test context.
 */
@WebMvcTest(com.shipmonk.testingday.controller.ExchangeRatesController.class)
@DisplayName("ExchangeRates Controller Tests")
class ExchangeRatesControllerTest {

    // In later Spring version, would use constructor injection
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExchangeRateService exchangeRateService;

    /**
     * Test 1: Successful retrieval of exchange rates
     *
     * Verifies that the endpoint returns 200 OK with properly formatted exchange rates.
     */
    @Test
    @DisplayName("Should return 200 OK with exchange rates for valid date")
    void testGetRatesSuccess() throws Exception {
        // Arrange: Prepare test data
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("1.04"));
        rates.put("GBP", new BigDecimal("0.85"));
        rates.put("JPY", new BigDecimal("140.75"));
        rates.put("CHF", new BigDecimal("0.92"));
        rates.put("CAD", new BigDecimal("1.42"));

        ExchangeRate testExchangeRate = new ExchangeRate("EUR", LocalDate.of(2022, 6, 20), rates);

        // Mock the service
        when(exchangeRateService.getExchangeRates("2022-06-20"))
            .thenReturn(testExchangeRate);

        // Act & Assert
        mockMvc.perform(get("/api/v1/rates/2022-06-20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.base").value("EUR"))
            .andExpect(jsonPath("$.date").value("2022-06-20"))
            .andExpect(jsonPath("$.rates").exists())
            .andExpect(jsonPath("$.rates.USD").value(1.04))
            .andExpect(jsonPath("$.rates.GBP").value(0.85))
            .andExpect(jsonPath("$.rates.JPY").value(140.75))
            .andExpect(jsonPath("$.rates.CHF").value(0.92))
            .andExpect(jsonPath("$.rates.CAD").value(1.42));
    }

    /**
     * Test 2: Handle not found exception
     *
     * Verifies that the endpoint returns 404 Not Found with error details
     * when exchange rates are not available.
     */
    @Test
    @DisplayName("Should return 404 Not Found when rates are not available")
    void testGetRatesNotFound() throws Exception {
        // Arrange: Configure mock to throw exception
        String dateRequested = "2020-01-01";
        when(exchangeRateService.getExchangeRates(dateRequested))
            .thenThrow(new ExchangeRateNotFoundException(dateRequested));

        // Act & Assert
        mockMvc.perform(get("/api/v1/rates/" + dateRequested))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("EXCHANGE_RATE_NOT_FOUND"))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.message", containsString("not found")))
            .andExpect(jsonPath("$.timestamp").exists());
    }
}
