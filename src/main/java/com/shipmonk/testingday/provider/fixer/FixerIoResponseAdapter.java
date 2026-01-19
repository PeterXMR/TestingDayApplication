package com.shipmonk.testingday.provider.fixer;

import com.shipmonk.testingday.api.ExchangeResponse;
import com.shipmonk.testingday.api.ExchangeRate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Adapter pattern: Converts Fixer.io API responses to our entity model.
 * This isolates our application from Fixer.io's response format.
 * If Fixer.io's API changes, we only need to update this adapter.
 */
public class FixerIoResponseAdapter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    /**
     * Adapts Fixer.io response to our ExchangeRate entity model.
     *
     * @param response the raw Fixer.io response
     * @return ExchangeRate entity object
     * @throws IllegalArgumentException if the response format is invalid
     */
    public static ExchangeRate adapt(ExchangeResponse response) {
        if (!response.isSuccess()) {
            throw new IllegalArgumentException(
                "Fixer.io API returned error: " +
                (response.getError() != null ? response.getError().getInfo() : "Unknown error")
            );
        }

        try {
            LocalDate date = LocalDate.parse(response.getDate(), DATE_FORMATTER);
            return new ExchangeRate(
                response.getBase(),
                date,
                response.getRates()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse Fixer.io response: " + e.getMessage(), e);
        }
    }
}

