package com.shipmonk.testingday;

import com.shipmonk.testingday.provider.fixer.FixerIoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FixerIoProperties.class})
public class TestingDayExchangeRatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingDayExchangeRatesApplication.class, args);
    }
}
