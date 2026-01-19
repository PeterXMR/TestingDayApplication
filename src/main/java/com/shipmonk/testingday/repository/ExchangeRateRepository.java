package com.shipmonk.testingday.repository;

import com.shipmonk.testingday.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing exchange rates persistence in PostgreSQL.
 * Uses Spring Data JPA with Hibernate ORM.
 */
@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, Long> {

    /**
     * Find all exchange rates for a specific date and base currency.
     */
    List<ExchangeRateEntity> findByBaseCurrencyAndDate(String baseCurrency, LocalDate date);

    /**
     * Find a specific exchange rate for a currency pair on a date.
     */
    Optional<ExchangeRateEntity> findByBaseCurrencyAndCurrencyAndDate(String baseCurrency, String currency, LocalDate date);
}
