package com.shipmonk.testingday.exception;

/**
 * Base exception for exchange rate provider errors.
 */
public class ExchangeRateProviderException extends RuntimeException {
    public ExchangeRateProviderException(String message) {
        super(message);
    }

    public ExchangeRateProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
