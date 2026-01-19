package com.shipmonk.testingday.exception;

/**
 * Exception thrown when exchange rates for a requested date cannot be found.
 */
public class ExchangeRateNotFoundException extends ExchangeRateProviderException {
    public ExchangeRateNotFoundException(String date) {
        super("Exchange rates not found for date: " + date);
    }
}

