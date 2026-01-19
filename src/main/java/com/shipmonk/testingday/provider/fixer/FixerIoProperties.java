package com.shipmonk.testingday.provider.fixer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Configuration properties for Fixer.io API integration.
 * Uses the historical rates endpoint: <a href="https://data.fixer.io/api/">...</a>{date}
 * Allows easy configuration via application.properties or environment variables.
 */
@Data
@ConfigurationProperties(prefix = "fixer.api")
public class FixerIoProperties {

    private String baseUrl;
    private String key;
    private String baseCurrency;

    public String getApiKey() {
        return key;
    }

    @Override
    public String toString() {
        return "FixerIoProperties{" +
                "baseUrl='" + baseUrl + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                '}';
    }
}
