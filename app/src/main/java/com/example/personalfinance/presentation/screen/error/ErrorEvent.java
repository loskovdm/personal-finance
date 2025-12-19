package com.example.personalfinance.presentation.screen.error;

public class ErrorEvent {
    private final String message;
    private final String code;

    public ErrorEvent(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() { return message; }
    public String getCode() { return code; }
}
