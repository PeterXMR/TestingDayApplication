package com.shipmonk.testingday.exception;

/**
 * Thrown when authentication with the exchange rate provider fails.
 * For example, when API key is invalid, expired, or missing.
 *
 * This indicates a configuration issue - retrying won't help.
 */
public class ExchangeRateAuthException extends RuntimeException {

    public ExchangeRateAuthException(String message) {
        super(message);
    }

}
