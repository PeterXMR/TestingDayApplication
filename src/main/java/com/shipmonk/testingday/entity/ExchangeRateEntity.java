package com.shipmonk.testingday.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * JPA entity for persisting exchange rates in PostgreSQL.
 * Stores historical exchange rate data from Fixer.io API.
 */
@Getter
//@NoArgsConstructor
@Entity
@Table(name = "exchange_rate", indexes = {
    @Index(name = "idx_currency_date", columnList = "currency,rate_date", unique = true),
    @Index(name = "idx_rate_date", columnList = "rate_date")
})
public class ExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baseCurrency;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, name = "rate_date")
    private LocalDate date;

    @Column(nullable = false, precision = 19, scale = 10)
    private BigDecimal rate;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    public ExchangeRateEntity(String baseCurrency, String currency, LocalDate date, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.currency = currency;
        this.date = date;
        this.rate = rate;
        this.createdAt = LocalDateTime.now();
    }
}

