package com.shipmonk.testingday.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetails {
    private int code;
    private String type;
    private String info;

    public ErrorDetails() {
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
               "code=" + code +
               ", type='" + type + '\'' +
               ", info='" + info + '\'' +
               '}';
    }
}