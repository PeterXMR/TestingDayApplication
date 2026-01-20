package com.shipmonk.testingday.controller;

import com.shipmonk.testingday.api.ExchangeRate;
import com.shipmonk.testingday.service.ExchangeRateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for exchange rates API.
 * Handles HTTP requests and delegates to the service layer.
 * The service layer is provider-agnostic due to the Strategy pattern.
 */
@RestController
@RequestMapping(path = "/api/v1/rates")
@Tag(name = "Exchange Rates", description = "API endpoints for retrieving exchange rates")
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
    @GetMapping("/{day}")
    @Operation(summary = "Get exchange rates for a specific date",
               description = "Retrieves exchange rates relative to EUR base currency for the specified date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved exchange rates",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeRate.class))),
        @ApiResponse(responseCode = "404", description = "Exchange rates not found for the specified date"),
        @ApiResponse(responseCode = "503", description = "Exchange rate service unavailable")
    })
    public ResponseEntity<ExchangeRate> getRates(
            @Parameter(description = "Date in ISO format (YYYY-MM-DD)", example = "2022-06-20", required = true)
            @PathVariable String day) {
        logger.info("ExchangeRatesController: Received request for exchange rates on date: {}", day);

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRates(day);

        logger.info("ExchangeRatesController: Returning {} exchange rates for date: {}",
            exchangeRate.getRates().size(), day);

        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }
}
