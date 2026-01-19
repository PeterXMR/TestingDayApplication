package com.shipmonk.testingday.provider;

import com.shipmonk.testingday.api.ExchangeRate;
import com.shipmonk.testingday.entity.ExchangeRateEntity;
import com.shipmonk.testingday.exception.ExchangeRateProviderException;
import com.shipmonk.testingday.provider.fixer.FixerIoProperties;
import com.shipmonk.testingday.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Decorator pattern: Wraps any ExchangeRateProvider with persistence logic.
 * This decorates the actual provider to store fetched rates in PostgreSQL fallback.
 *
 * Key benefits:
 * - Persistence is transparent to clients
 * - Can be applied to any provider implementation
 * - Acts as a fallback when API is unavailable
 * - Provides audit trail of all rates fetched
 */
@Component
public class PrimaryExchangeRateProvider implements ExchangeRateProvider {

    private static final Logger logger = LoggerFactory.getLogger(PrimaryExchangeRateProvider.class);

    private final ExchangeRateProvider delegate;
    private final ExchangeRateRepository repository;
    private final String baseCurrency;

    public PrimaryExchangeRateProvider(
            ExchangeRateProvider delegate,
            ExchangeRateRepository repository,
            FixerIoProperties properties) {
        this.delegate = delegate;
        this.repository = repository;
        this.baseCurrency = properties.getBaseCurrency();
    }

    @Override
    public ExchangeRate getExchangeRates(LocalDate date) {
        logger.info("PersistenceProvider: Attempting to retrieve rates for date: {}", date);

        // Try to get from fallback first
        List<ExchangeRateEntity> savedRates = repository.findByBaseCurrencyAndDate(baseCurrency, date);

        if (!savedRates.isEmpty()) {
            logger.info("Database HIT: Found {} saved rates for date: {}", savedRates.size(), date);
            return buildExchangeRateFromDatabase(savedRates);
        }

        logger.info("Database MISS: No saved rates for date: {}, fetching from provider", date);

        try {
            // Fetch from the wrapped provider
            ExchangeRate exchangeRate = delegate.getExchangeRates(date);

            // Persist the result
            persistExchangeRates(exchangeRate);

            logger.info("Successfully persisted {} exchange rates for date: {}", exchangeRate.getRates().size(), date);
            return exchangeRate;

        } catch (ExchangeRateProviderException e) {

            logger.error("Provider failed to fetch rates for date: {}", date);
            throw e;
        }
    }

    private void persistExchangeRates(ExchangeRate exchangeRate) {
        for (Map.Entry<String, BigDecimal> rateEntry : exchangeRate.getRates().entrySet()) {
            // Check if rate already exists
            Optional<ExchangeRateEntity> existing = repository.findByBaseCurrencyAndCurrencyAndDate(
                exchangeRate.getBaseCurrency(),
                rateEntry.getKey(),
                exchangeRate.getDate()
            );

            if (existing.isEmpty()) {
                // Only save if not already in fallback
                ExchangeRateEntity entity = new ExchangeRateEntity(
                    exchangeRate.getBaseCurrency(),
                    rateEntry.getKey(),
                    exchangeRate.getDate(),
                    rateEntry.getValue()
                );
                repository.save(entity);
            }
        }
    }

    private ExchangeRate buildExchangeRateFromDatabase(List<ExchangeRateEntity> savedRates) {
        Map<String, BigDecimal> rates = new java.util.HashMap<>();
        LocalDate date = null;

        for (ExchangeRateEntity entity : savedRates) {
            rates.put(entity.getCurrency(), entity.getRate());
            if (date == null) {
                date = entity.getDate();
            }
        }

        return new ExchangeRate(baseCurrency, date, rates);
    }
}

