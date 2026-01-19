package com.shipmonk.testingday.api;

import lombok.Data;

@Data
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