package com.shipmonk.testingday.service;

import com.shipmonk.testingday.api.ExchangeRate;
import com.shipmonk.testingday.exception.ExchangeRateProviderException;
import com.shipmonk.testingday.provider.ExchangeRateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Service layer for exchange rate operations.
 * Encapsulates business logic and error handling.
 * Depends on the ExchangeRateProvider interface (Strategy pattern), not concrete implementations.
 */
@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private final ExchangeRateProvider exchangeRateProvider;

    public ExchangeRateService(ExchangeRateProvider exchangeRateProvider) {
        this.exchangeRateProvider = exchangeRateProvider;
    }

    /**
     * Get exchange rates for a given date.
     *
     * @param dateString date in ISO format (YYYY-MM-DD)
     * @return ExchangeRate with rates for the date
     * @throws IllegalArgumentException if the date format is invalid
     * @throws ExchangeRateProviderException if rates cannot be retrieved from any source
     */
    public ExchangeRate getExchangeRates(String dateString) {
        LocalDate date = parseDate(dateString);
        validateDate(date);

        logger.info("ExchangeRateService: Retrieving rates for date: {}", date);
        return exchangeRateProvider.getExchangeRates(date);
    }

    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.warn("Invalid date format provided: {}", dateString);
            throw new IllegalArgumentException(
                "Invalid date format. Expected format: YYYY-MM-DD, got: " + dateString,
                e
            );
        }
    }

    private void validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            logger.warn("Requested date is in the future: {}", date);
            throw new IllegalArgumentException(
                "Cannot retrieve exchange rates for future dates. Requested date: " + date
            );
        }
    }
}
