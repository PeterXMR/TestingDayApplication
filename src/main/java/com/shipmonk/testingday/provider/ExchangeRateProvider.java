package com.shipmonk.testingday.provider;

import com.shipmonk.testingday.api.ExchangeRate;
import java.time.LocalDate;

/**
 * Strategy interface for exchange rate providers.
 * Allows different implementations to be plugged in without changing client code.
 *
 * This is the core abstraction that enables easy provider swapping.
 */
public interface ExchangeRateProvider {

    /**
     * Retrieves exchange rates for a given date with EUR as the base currency.
     * EUR is the default base currency supported by the free tier of Fixer.io API.
     *
     * @param date the date for which to retrieve rates
     * @return ExchangeRate containing rates for the given date
     * @throws com.shipmonk.testingday.exception.ExchangeRateProviderException if rates cannot be retrieved
     */
    ExchangeRate getExchangeRates(LocalDate date);
}

