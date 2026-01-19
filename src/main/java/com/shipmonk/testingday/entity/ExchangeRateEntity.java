package com.shipmonk.testingday.entity;

import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * JPA entity for persisting exchange rates in PostgreSQL.
 * Stores historical exchange rate data from Fixer.io API.
 */
@Getter
@Setter
@RequiredArgsConstructor
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

    @Override
    public String toString() {
        return "ExchangeRateEntity{" +
                "id=" + id +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", currency='" + currency + '\'' +
                ", date=" + date +
                ", rate=" + rate +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null) {return false;}
        Class<?> oEffectiveClass = o instanceof HibernateProxy ?
                                   ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() :
                                   o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                                      ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() :
                                      this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {return false;}
        ExchangeRateEntity that = (ExchangeRateEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
               ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
               getClass().hashCode();
    }
}

