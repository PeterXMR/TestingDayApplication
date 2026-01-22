package com.shipmonk.testingday.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

/**
 * Circuit Breaker Configuration for External API Calls.
 * PROBLEM: If Fixer.io API fails, repeated calls waste resources and degrade UX.
 * SOLUTION: Circuit Breaker pattern prevents cascading failures and fast-fails.
 * States: CLOSED (normal), OPEN (rejecting), HALF_OPEN (testing recovery).
 * Configuration (for 100-500 RPS): Failure threshold 50%, slow call >2s, window 100 calls, wait 30s recovery, 3 half-open permits.
 * Effect: Prevents thundering herd on recovery, reduces cascading failures, fast rejection saves ~100ms per call.
 */
@Configuration
public class CircuitBreakerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerConfigurer.class);

    /**
     * Circuit breaker for Fixer.io API calls.
     * Thresholds: 50% failure rate, 50% slow calls (>2s), 100-call sliding window.
     * Recovery: 30 second wait duration, 3 half-open permits.
     * Exception handling: Records all exceptions, ignores IllegalArgumentException.
     * Logging: All state transitions logged for monitoring.
     */
    @Bean
    public CircuitBreaker fixerIoCircuitBreaker() {
        CircuitBreakerConfig cbConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .slowCallRateThreshold(50)
            .slowCallDurationThreshold(Duration.ofSeconds(2))
            .slidingWindowSize(100)
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .permittedNumberOfCallsInHalfOpenState(3)
            .recordExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .build();

        CircuitBreaker circuitBreaker = CircuitBreaker.of("fixer-io", cbConfig);

        // Log circuit breaker state changes
        circuitBreaker.getEventPublisher()
            .onStateTransition(event -> logger.warn(
                "Circuit breaker state transition: {} -> {}",
                event.getStateTransition().getFromState(),
                event.getStateTransition().getToState()
            ));

        return circuitBreaker;
    }
}


