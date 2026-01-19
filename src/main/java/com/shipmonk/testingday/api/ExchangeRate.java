package com.shipmonk.testingday.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import lombok.Data;

/**
 * Domain model for exchange rates.
 * Represents exchange rates for a specific date with rates relative to base currency.
 */
@Data
public class ExchangeRate {

    @JsonProperty("base")
    private String baseCurrency;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("rates")
    private Map<String, BigDecimal> rates;

    public ExchangeRate(String baseCurrency, LocalDate date, Map<String, BigDecimal> rates) {
        this.baseCurrency = baseCurrency;
        this.date = date;
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "baseCurrency='" + baseCurrency + '\'' +
                ", date=" + date +
                ", rates=" + rates +
                '}';
    }
}

