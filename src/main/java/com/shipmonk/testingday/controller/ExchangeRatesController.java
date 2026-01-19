package com.shipmonk.testingday.controller;

import com.shipmonk.testingday.api.ExchangeRate;
import com.shipmonk.testingday.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for exchange rates API.
 * Handles HTTP requests and delegates to the service layer.
 * The service layer is provider-agnostic due to the Strategy pattern.
 */
@RestController
@RequestMapping(path = "/api/v1/rates")
public class ExchangeRatesController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRatesController.class);

    private final ExchangeRateService exchangeRateService;

    public ExchangeRatesController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Get exchange rates for a specific date.
     *
     * @param day date in ISO format (YYYY-MM-DD)
     * @return ExchangeRate response with EUR as base currency and rates for other currencies
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{day}")
    public ResponseEntity<ExchangeRate> getRates(@PathVariable("day") String day) {
        logger.info("ExchangeRatesController: Received request for exchange rates on date: {}", day);

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRates(day);

        logger.info("ExchangeRatesController: Returning {} exchange rates for date: {}",
            exchangeRate.getRates().size(), day);

        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }
}
