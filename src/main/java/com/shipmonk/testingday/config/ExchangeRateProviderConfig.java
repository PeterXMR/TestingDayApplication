package com.shipmonk.testingday.config;

import com.shipmonk.testingday.provider.ExchangeRateProvider;
import com.shipmonk.testingday.provider.PrimaryExchangeRateProvider;
import com.shipmonk.testingday.provider.fixer.FixerIoExchangeRateProvider;
import com.shipmonk.testingday.provider.fixer.FixerIoProperties;
import com.shipmonk.testingday.repository.ExchangeRateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Spring configuration for exchange rate providers.
 * Implements Decorator pattern for provider abstraction and data persistence.
 *
 * Current setup: FixerIoExchangeRateProvider wrapped with persistence via PostgreSQL.
 *
 * To change the provider, simply modify the bean definition below.
 * The decorator will work with any provider implementation.
 */
@Configuration
public class ExchangeRateProviderConfig {

    @Bean
    @Primary
    public ExchangeRateProvider exchangeRateProvider(
            FixerIoExchangeRateProvider fixerIoProvider,
            ExchangeRateRepository repository,
            FixerIoProperties properties) {

        // Decorator pattern: wrap the Fixer.io provider with persistence
        return new PrimaryExchangeRateProvider(fixerIoProvider, repository, properties);
    }
}

