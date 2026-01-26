package com.shipmonk.testingday.provider.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipmonk.testingday.api.ExchangeResponse;
import com.shipmonk.testingday.api.ExchangeRate;
import com.shipmonk.testingday.exception.ExchangeRateAuthException;
import com.shipmonk.testingday.exception.ExchangeRateNotFoundException;
import com.shipmonk.testingday.exception.ExchangeRateProviderException;
import com.shipmonk.testingday.provider.ExchangeRateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Concrete implementation of ExchangeRateProvider that fetches rates from Fixer.io.
 * Uses the historical rates endpoint: <a href="https://data.fixer.io/api/">...</a>{date}
 * This is one concrete strategy that can be swapped for another provider.
 */
@Component
public class FixerIoExchangeRateProvider implements ExchangeRateProvider {

    private static final Logger logger = LoggerFactory.getLogger(FixerIoExchangeRateProvider.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String baseUrl;
    private final String baseCurrency;

    public FixerIoExchangeRateProvider(
            RestTemplateBuilder restTemplateBuilder,
            FixerIoProperties properties,
            ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.apiKey = properties.getApiKey();
        this.baseUrl = properties.getBaseUrl();
        this.baseCurrency = properties.getBaseCurrency();
    }

    @Override
    public ExchangeRate getExchangeRates(LocalDate date) {
        try {
            logger.info("Fetching exchange rates from Fixer.io for date: {}", date);

            String url = buildUrl(date);
            logger.debug("Calling Fixer.io API: {}", url.replaceAll(apiKey, "***"));

            ExchangeResponse response = restTemplate.getForObject(url, ExchangeResponse.class);

            if (response == null) {
                throw new ExchangeRateNotFoundException(date.toString());
            }

            ExchangeRate exchangeRate = FixerIoResponseAdapter.adapt(response);
            logger.info("Successfully fetched exchange rates for date: {} with {} rates", date, exchangeRate.getRates().size());

            return exchangeRate;

        } catch (ExchangeRateProviderException | ExchangeRateAuthException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error from Fixer.io for date: {}, status: {}", date, e.getStatusCode(), e);
            handleHttpClientError(e);
            // If handleHttpClientError doesn't throw, throw generic exception
            throw new ExchangeRateProviderException(
                "Failed to fetch exchange rates from Fixer.io: " + e.getMessage(),
                e
            );
        } catch (RestClientException e) {
            logger.error("Failed to fetch exchange rates from Fixer.io for date: {}", date, e);
            throw new ExchangeRateProviderException(
                "Failed to fetch exchange rates from Fixer.io: " + e.getMessage(),
                e
            );
        } catch (Exception e) {
            logger.error("Unexpected error fetching exchange rates for date: {}", date, e);
            throw new ExchangeRateProviderException(
                "Unexpected error fetching exchange rates: " + e.getMessage(),
                e
            );
        }
    }

    /**
     * Handles HTTP client errors (4xx) from Fixer.io.
     * Parses the response body to determine the specific error type.
     */
    private void handleHttpClientError(HttpClientErrorException e) {
        try {
            String responseBody = e.getResponseBodyAsString();
            ExchangeResponse errorResponse = objectMapper.readValue(responseBody, ExchangeResponse.class);

            // Delegate to adapter for consistent error handling
            FixerIoResponseAdapter.handleApiError(errorResponse);

        } catch (ExchangeRateAuthException | ExchangeRateProviderException ex) {
            throw ex;
        } catch (Exception parseEx) {
            logger.warn("Could not parse Fixer.io error response: {}", parseEx.getMessage());
        }
    }

    private String buildUrl(LocalDate date) {
        String formattedDate = date.format(DATE_FORMATTER);
        // Format: https://data.fixer.io/api/2013-12-24?access_key=XXX&base=USD
        return String.format("%s/%s?access_key=%s&base=%s",
            baseUrl,
            formattedDate,
            apiKey,
            baseCurrency
        );
    }
}
