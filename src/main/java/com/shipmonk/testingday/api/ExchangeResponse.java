package com.shipmonk.testingday.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the response from Fixer.io API.
 * This class adapts the external API response format to our needs.
 */
@Data
@NoArgsConstructor
public class ExchangeResponse {

    private boolean success;

    private String base;

    private String date;

    private Map<String, BigDecimal> rates;

    @JsonProperty("error")
    private ErrorDetails error;

    @Override
    public String toString() {
        return "ExchangeResponse{" +
                "success=" + success +
                ", base='" + base + '\'' +
                ", date='" + date + '\'' +
                ", rates=" + rates +
                ", error=" + error +
                '}';
    }
}
