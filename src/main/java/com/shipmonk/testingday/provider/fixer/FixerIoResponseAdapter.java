package com.shipmonk.testingday.provider.fixer;

import com.shipmonk.testingday.api.ExchangeResponse;
import com.shipmonk.testingday.api.ExchangeRate;
import com.shipmonk.testingday.exception.ExchangeRateAuthException;
import com.shipmonk.testingday.exception.ExchangeRateProviderException;

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
     * @throws ExchangeRateAuthException if API key is invalid or expired
     * @throws ExchangeRateProviderException if the API returns other errors
     */
    public static ExchangeRate adapt(ExchangeResponse response) {
        if (!response.isSuccess()) {
            handleApiError(response);
        }

        try {
            LocalDate date = LocalDate.parse(response.getDate(), DATE_FORMATTER);
            return new ExchangeRate(
                response.getBase(),
                date,
                response.getRates()
            );
        } catch (Exception e) {
            throw new ExchangeRateProviderException("Failed to parse Fixer.io response: " + e.getMessage(), e);
        }
    }

    /**
     * Handles Fixer.io API errors by throwing appropriate exceptions.
     * Fixer.io error codes:
     * - 101: Invalid API key
     * - 104: Usage limit exceeded
     * - Other: General provider errors
     *
     * @param response the error response from Fixer.io
     * @throws ExchangeRateAuthException for auth-related errors
     * @throws ExchangeRateProviderException for other errors
     */
    public static void handleApiError(ExchangeResponse response) {
        String errorMessage = response.getError() != null
            ? response.getError().getInfo()
            : "Unknown error";

        int errorCode = response.getError() != null
            ? response.getError().getCode()
            : 0;

        // Auth-related errors (invalid key, expired, usage limit)
        if (errorCode == 101 || errorCode == 104) {
            throw new ExchangeRateAuthException("Fixer.io authentication failed: " + errorMessage);
        }

        // All other errors
        throw new ExchangeRateProviderException("Fixer.io API error: " + errorMessage);
    }
}

